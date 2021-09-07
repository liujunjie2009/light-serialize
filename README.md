# Light-serialize
Light-serialize是一个高性能、简便易用的二进制序列化库，它可以快速的将对象序列化为二进制，也可以快速的将二进制反序列化为对象。
它具有小巧，无三方依赖，运行快，支持精确、按属性名称、按属性顺序序列化，在时间、空间、海量数据序列化、使用的便利性、
0拷贝的场景、减少垃圾内存碎片的产生、降级GC频率中有较大优势。支持循环引用。
Light-serialize只支持Java语言。目前支持java8。

# 基准测试
基于jmh对Light-serialize、Kryo(5.1.1)、Hessian(4.0.65)、Protostuff-runtime(1.7.4)进行了基准测试，操作系统为Macos 11.1，处理器为2.6 GHz 六核Intel Core i7，
内存为16 GB 2667 MHz DDR4，未对JDK做参数调优。相关测试代码在light-serialize-benchmark中。

## 更快(以后)、更小
目前在速度上不是最优，主要是目前没有做优化，如堆外内存、字节码操作、采用自定义Hash等。

| 项目                                           | 平均QPS对比                 | 字节对比       |
|----------------------------------------------|-------------------------|------------|
| Light-serialize(Exact) VS Kryo               | 31259.466 VS 49328.099  | 155 VS 256 |
| Light-serialize(Order) VS Protostuff-runtime | 14318.708 VS 5517.297   | 113 VS 229 |
| Light-serialize(Name) VS Hessian             | 14045.578 VS 16579.992  | 506 VS 555 |


## 功能更强
Light-serialize在提供更多序列化方式满足不同场景的同时，修复了对比库的一些不足。

| 项目                 | 支持精确序列化 | 支持按字段名称序列化 | 支持按字段顺序序列化 | 备注                                                    |
|--------------------|---------|------------|------------|-------------------------------------------------------|
| Light-serialize    | 是       | 是          | 是          | 新项目，稳定性、可靠性待商榷                                        |
| Kryo               | 是       | 否          | 否          | Kyro的序列化器注册id采用递增的机制，不同的机器、不同的时间都可能导致注册的id不一样，最终序列化失败 |
| Protostuff-runtime | 否       | 是          | 否          | 父类添加或删除字段所有子类序列化都可能失败                                 |
| Hessian            | 否       | 否          | 是          | 父子类同名，属性丢失                                            |



#文档

*  [下载](#下载)
   * maven
   * Gradle
* [快速开始](#快速开始)
* [IO介绍](#IO介绍)
   * [Buffer](#下载)
      * [LinkedByteBuffer](#下载)
   * [ObjectInput&ReadContext](#ObjectInput&ReadContext)
   * [ObjectOutput&WriteContext](#ObjectOutput&WriteContext)
* [Serialize(序列化)](#Serialize(序列化))
   * [序列化协议](#序列化协议)
   * [数据编码](#数据编码)
       * [整数变长编码](#整数变长编码)
       * [浮点数变长编码](#浮点数变长编码)
       * [字符串编码](#字符串编码)
   * [序列化](#序列化)
      * [运行时根据数值选择最优的序列化类型](#运行时根据数值选择最优的序列化类型)   
      * [预定义Tag类型和注册常用类型](#预定义Tag类型和注册常用类型)
      * [序列化策略(Name、Order、Exact)](#序列化策略(Name、Order、Exact))
      * [对象引用关系序列化](#对象引用关系序列化)
      * [类型引用序列化](#类型引用序列化)
      * [字段名称引用序列化](#字段名称引用序列化)
      
   * [Serializers](#Serializers)
      * [ObjectSerializer](#ObjectSerializer)
      * [自定义序列化Serializer](#自定义序列化Serializer)
* [instantiator](#instantiator)
* [最佳实战](#最佳实战)
* [基于netty封装0拷贝的Buffer实现RPC](#基于netty封装0拷贝的Buffer实现RPC)




# 下载

# 快速开始
HelloLightSerialize 默认采用按字段名称进行序列化。

## 使用工具方法快速序列化
```
package org.light.serialize.example;

import org.light.serialize.core.util.SerializeUtil;

import java.io.IOException;

public class HelloLightSerialize {

    public static void main(String[] args) throws IOException {
        String hello = "hello light serialize";
        byte[] bytes = SerializeUtil.serialize(hello);
        String target = (String) SerializeUtil.deserialize(bytes);

        assert hello.equals(target);
    }

}
```

## 手动创建ObjectOutput、ObjectInput对象来更灵活的序列化
```
package org.light.serialize.example;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;

public class HelloLightSerialize2 {

    public static void main(String[] args) throws IOException {
        String hello = "hello light serialize";

        ObjectOutput output = new ObjectOutput(64);
        output.writeObject(hello);

        ObjectInput input = new ObjectInput(output.buffer());
        String target = (String) input.readObject();

        assert hello.equals(target);
    }

}
```

### 采用实例化器直接序列化
如果在序列化过程中，类型固定，可以使用序列化器直接序列化，可以减少类型信息的传输。
```
package org.light.serialize.example;

import org.light.serialize.core.constants.Strategy;
import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.java.BasicSerializers;
import org.light.serialize.core.serializer.java.ObjectSerializer;

import java.io.IOException;

public class DirectSerializerExample {

    public static void main(String[] args) throws IOException {
        ObjectOutput output = new ObjectOutput(512);
        BasicSerializers.LONG_SERIALIZER.write(output, 100L, Strategy.ORDER);
        Long read = BasicSerializers.LONG_SERIALIZER.read(new ObjectInput(output.buffer()));
        assert read == 100L
    }

}
```

# IO介绍

## Buffer
Buffer是字节缓冲数据的抽象，它提供了核心的读取数据方法和写入数据方法，特别注意的是它提供了读取索引和写入索引,如果我们是在同一个Buffer中读取和写入数据，可以避免数据的拷贝。


### LinkedByteBuffer
LinkedByteBuffer类实现了Buffer接口，它基于链表结构，在对Java对象进行顺序的读取和写入时，能够"0拷贝数据"。当Buffer容量不足时，向链表尾部追加
一定容量的字节。扩容策略是32字节乘以2的倍数，最大扩容容量是8Mb。
读和写索引描述：TODO


## ObjectInput&ReadContext
ObjectInput持有一个Buffer引用，封装了数据的读取方法。ReadContext是基于ThreadLocal的类，它提供了在读取数据的过程中上下文信息传递。如对象的引用
信息，当前序列化对象的深度等。

## ObjectOutput&WriteContext
ObjectOutput同样持有一个Buffer引用，封装了数据的写入方法。WriteContext也是基于ThreadLocal的类，它提供了在写入数据的过程中上下文信息传递。
如对象的引用信息，当前序列化对象的深度等。


# Serialize(序列化)

## 序列化协议
对象在被序列化时，如果序列化数据含有类型，数据分为Tag和Body，如果预先知道数据类型则只有Body部分。Tag为固定1个字节，Body可以0个或多个字节。Tag如下:

| tag name                        | tag value | 描述                      |
|---------------------------------|-----------|-------------------------|
| NULL                            | -128      | null                    |
| BOOL                            | -127      | boolean基本类型             |
| BOOL_WRAPPER                    | -126      | boolean装箱类型             |
| BOOL_TRUE                       | -125      | true                    |
| BOOL_FALSE                      | -124      | false                   |
| BYTE                            | -123      | byte基本类型                |
| BYTE_WRAPPER                    | -122      | byte装箱类型                |
| BYTE_0                          | -121      | byte: 0                 |
| BYTE_1                          | -120      | byte: 1                 |
| BYTE_N_1                        | -119      | byte: -1                |
| CHAR                            | -118      | char基本类型                |
| CHAR_WRAPPER                    | -117      | char装箱类型                |
| CHAR_ASCII                      | -116      | ASCII char              |
| SHORT                           | -115      | short基本类型               |
| SHORT_WRAPPER                   | -114      | short装箱类型               |
| SHORT_BYTE                      | -113      | 在byte范围内的short          |
| SHORT_REVERSE_VAR               | -112      | short reverse后变长编码类型    |
| SHORT_COMPLEMENT_REVERSE_VAR    | -111      | short 取反后reverse后变长编码类型 |
| SHORT_0                         | -110      | short: 0                |
| SHORT_1                         | -109      | short: 1                |
| SHORT_N_1                       | -108      | short: -1               |
| INT                             | -107      | int基本类型                 |
| INT_WRAPPER                     | -106      | int装箱类型                 |
| INT_REVERSE_VAR                 | -105      | int reverse后变长编码类型      |
| INT_COMPLEMENT_REVERSE_VAR      | -104      | int取反后reverse后变长编码类型    |
| INT_N_16                        | -103      | int: -16                |
| INT_47                          | -40       | int: 47                 |
| INT_N_2048                      | -39       | int: -2048              |
| INT_2047                        | -24       | int: 2047               |
| INT_N_262144                    | -23       | int: -262144            |
| INT_262143                      | -16       | int: 262143             |
| INT_N_33554432                  | -15       | int: -33554432          |
| INT_33554433                    | -12       | int: 33554433           |
| LONG                            | -11       | long基本类型                |
| LONG_WRAPPER                    | -10       | long装箱类型                |
| LONG_REVERSE_VAR                | -9        | long reverse后变长编码类型     |
| LONG_COMPLEMENT_REVERSE_VAR     | -8        | long取反后reverse后变长编码类型   |
| LONG_N_4                        | -7        | int: -4                 |
| LONG_11                         | 8         | int: 11                 |
| LONG_N_1024                     | 9         | int: -1024              |
| LONG_1023                       | 16        | int: 1023               |
| LONG_N_131072                   | 17        | int: -131072            |
| LONG_131071                     | 20        | int: 131071             |
| LONG_N_16777216                 | 21        | int: -16777216          |
| LONG_16777215                   | 22        | int: 16777215           |
| LONG_INT                        | 23        | 在int范围内的long类型          |
| LONG_5_BYTES                    | 24        | 5个字节表示的long类型           |
| LONG_6_BYTES                    | 25        | 6个字节表示的long类型           |
| LONG_7_BYTES                    | 26        | 7个字节表示的long类型           |
| FLOAT                           | 27        | float基本类型               |
| FLOAT_WRAPPER                   | 28        | float装箱类型               |
| FLOAT_0                         | 29        | float: 0                |
| FLOAT_1                         | 30        | float: 1                |
| FLOAT_N_1                       | 31        | float: -1               |
| FLOAT_BYTE                      | 32        | 在byte范围内的float          |
| FLOAT_SHORT                     | 33        | 在short范围内的float         |
| FLOAT_MILLI_VAR                 | 34        | float乘以1000后变长编码类型      |
| FLOAT_MILLI_COMPLEMENT_VAR      | 35        | float乘以1000取反后变长编码类型    |
| DOUBLE                          | 36        | double基本类型              |
| DOUBLE_WRAPPER                  | 37        | double装箱类型              |
| DOUBLE_0                        | 38        | double: 0               |
| DOUBLE_1                        | 39        | double 1                |
| DOUBLE_N_1                      | 40        | double: -1              |
| DOUBLE_BYTE                     | 41        | 在byte范围内的double         |
| DOUBLE_SHORT                    | 42        | 在short范围内的double        |
| DOUBLE_MILLI_VAR                | 43        | double乘以1000后变长编码类型     |
| DOUBLE_MILLI_COMPLEMENT_VAR     | 44        | double乘以1000取反后变长编码类型   |
| STRING                          | 45        | 字符串类型                   |
| STRING_EMPTY                    | 46        | 空字符串                    |
| OBJECT_REFERENCE                | 47        | 对象引用类型                  |
| OBJECT_REFERENCE_0              | 48        | 第0个对象引用                 |
| OBJECT_REFERENCE_1              | 49        | 第1个对象引用                 |
| OBJECT_REFERENCE_2              | 50        | 第2个对象引用                 |
| OBJECT_REFERENCE_3              | 51        | 第3个对象引用                 |
| OBJECT_REFERENCE_4              | 52        | 第4个对象引用                 |
| OBJECT_REFERENCE_5              | 53        | 第5个对象引用                 |
| OBJECT_REFERENCE_6              | 54        | 第6个对象引用                 |
| OBJECT_REFERENCE_7              | 55        | 第7个对象引用                 |
| OBJECT_REFERENCE_LAST_0         | 56        | 倒数第0个对象引用               |
| OBJECT_REFERENCE_LAST_1         | 57        | 倒数第1个对象引用               |
| OBJECT_REFERENCE_LAST_2         | 58        | 倒数第2个对象引用               |
| OBJECT_REFERENCE_LAST_3         | 59        | 倒数第3个对象引用               |
| OBJECT_REFERENCE_LAST_4         | 60        | 倒数第4个对象引用               |
| OBJECT_REFERENCE_LAST_5         | 61        | 倒数第5个对象引用               |
| OBJECT_REFERENCE_LAST_6         | 62        | 倒数第6个对象引用               |
| OBJECT_REFERENCE_LAST_7         | 63        | 倒数第7个对象引用               |
| TYPE_REFERENCE                  | 64        | 类型引用类型                  |
| TYPE_REFERENCE_0                | 65        | 第0个类型引用                 |
| TYPE_REFERENCE_1                | 66        | 第1个类型引用                 |
| TYPE_REFERENCE_2                | 67        | 第2个类型引用                 |
| TYPE_REFERENCE_3                | 68        | 第3个类型引用                 |
| TYPE_REFERENCE_4                | 69        | 第4个类型引用                 |
| TYPE_REFERENCE_5                | 70        | 第5个类型引用                 |
| TYPE_REFERENCE_6                | 71        | 第6个类型引用                 |
| TYPE_REFERENCE_7                | 72        | 第7个类型引用                 |
| TYPE_REFERENCE_LAST_0           | 73        | 倒数第0个类型引用               |
| TYPE_REFERENCE_LAST_1           | 74        | 倒数第1个类型引用               |
| TYPE_REFERENCE_LAST_2           | 75        | 倒数第2个类型引用               |
| TYPE_REFERENCE_LAST_3           | 76        | 倒数第3个类型引用               |
| TYPE_REFERENCE_LAST_4           | 77        | 倒数第4个类型引用               |
| TYPE_REFERENCE_LAST_5           | 78        | 倒数第5个类型引用               |
| TYPE_REFERENCE_LAST_6           | 79        | 倒数第6个类型引用               |
| TYPE_REFERENCE_LAST_7           | 80        | 倒数第7个类型引用               |
| TYPE_REGISTERED                 | 81        | 注册过的类型                  |
| TYPE_NAME                       | 82        | 类型名称                    |
| VOID                            | 83        | void基本类型                |
| VOID_WRAPPER                    | 84        | void装箱类型                |
| CLASS                           | 85        | Class类型                 |
| OBJECT                          | 86        | Object类型                |
| BOOL_ARRAY                      | 87        | boolean基本类型数组           |
| BOOL_WRAPPER_ARRAY              | 88        | boolean装箱类型数组           |
| BYTE_ARRAY                      | 89        | byte基本类型数组              |
| BYTE_WRAPPER_ARRAY              | 90        | byte装箱类型数组              |
| CHAR_ARRAY                      | 91        | char基本类型数组              |
| CHAR_WRAPPER_ARRAY              | 92        | char装箱类型数组              |
| SHORT_ARRAY                     | 93        | short基本类型数组             |
| SHORT_WRAPPER_ARRAY             | 94        | short装箱类型数组             |
| INT_ARRAY                       | 95        | int基本类型数组               |
| INT_WRAPPER_ARRAY               | 96        | int装箱类型数组               |
| LONG_ARRAY                      | 97        | long基本类型数组              |
| LONG_WRAPPER_ARRAY              | 98        | long装箱类型数组              |
| FLOAT_ARRAY                     | 99        | float基本类型数组             |
| FLOAT_WRAPPER_ARRAY             | 100       | float装箱类型数组             |
| DOUBLE_ARRAY                    | 101       | double基本类型数组            |
| DOUBLE_WRAPPER_ARRAY            | 102       | double装箱类型数组            |
| STRING_ARRAY                    | 103       | 字符串数组                   |
| CLASS_ARRAY                     | 104       | Class数组                 |
| OBJECT_ARRAY                    | 105       | Object数组                |
| HASH_SET                        | 106       | HashSet                 |
| LINKED_HASH_SET                 | 107       | LinkedHashSet           |
| TREE_SET                        | 108       | TreeSet                 |
| ARRAY_LIST                      | 109       | ArrayList               |
| LINKED_LIST                     | 110       | LinkedList              |
| VECTOR                          | 111       | Vector                  |
| HASH_MAP                        | 112       | HashMap                 |
| HASH_TABLE                      | 113       | HashTable               |
| LINKED_HASH_MAP                 | 114       | LinkedHashMap           |
| CONCURRENT_HASH_MAP             | 115       | ConcurrentHashMap       |
| TREE_MAP                        | 116       | TreeMap                 |
| OPTIONAL                        | 117       | Optional                |
| OPTIONAL_DOUBLE                 | 118       | OptionalDouble          |
| OPTIONAL_INT                    | 119       | OptionalInt             |
| OPTIONAL_LONG                   | 120       | OptionalInt             |
| DATE                            | 121       | Date                    |
| GREGORIAN_CALENDAR              | 122       | GregorianCalendar       |
| BIG_DECIMAL                     | 123       | BigDecimal              |
| BIG_INTEGER                     | 124       | BigInteger              |
| PROXY                           | 125       | Proxy                   |
| LAMBDA                          | 126       | lambda表达式               |



## 数据编码

### 整数变长编码
整数采用的是变长编码,分为3类:
+ 普通变长编码，对正整数有优势，但对负数会使用更多字节。
+ 相反数变长编码，先将数字取反，然后再使用变长数字吗，对负数有优势，但正整数会使用更多字节。
+ 使用Zigzag变长编码，先将数字进行Zigzag编码，然后进行变长编码，对整数和负数都有优势，但有效范围减半。


# 浮点数变长编码
Java的浮点数采用IEE754编码，小数部分为0等可以用较少Bit表示时，是可以进行压缩的。将浮点数的字节转化为整数，再将整数的字节
倒序后进行整数的变长编码。


### boolean基本类型和装箱类型优化 
布尔类型在序列化时已经知道序列化的值(true或false)，只需序列化类型即可.布尔类型数组优化为每个值一个Bit位表示，布尔包装类型
数组优化为每个值使用2个Bit为表示。布尔类型数组和布尔包装类型数组在序列化时优化为使用Byte数组序列化。对于布尔数组可以使用约
原大小的1/8，布尔包装类型数组可以使用约原大小的1/4.


### 字符串编码
当字符串的长度小于32时，优先使用ASCII编码。


## 序列化

## 运行时根据数值选择最优的序列化类型
当序列化一个数字时，根据数字的大小采用最佳的数字编码，极大的压缩数字的传输大小。

### 预定义Tag类型和注册常用类型
当对一个对象进行序列化时，会序列化对象的类型，为了减小序列化类型的大小，对已有的常用的类型预定义了类型ID，对于Java比较常用的类型，如：
HashMap、ArrayList等，采用Tag直接表示，JDk其他类型采用XxHash 32位算法计算的整形id, 对于非Java类型采用XxHash 64位算法计算的整形id。


### 序列化策略(Name、Order、Exact)
Light-serialize支持3中序列化策略：

| 策略    | 描述                                       | 兼容性 | 序列化字节大小 |
|-------|------------------------------------------|-----|---------|
| Name  | 根据对象的字段名称序列化可以调整字段的顺序、修改字段的名称            | 好   | 大       |
| Order | 根据对象的字段顺序序列化，兼容性好，可以添加字段                 | 中   | 中       |
| Exact | 严格匹配字段，添加、删除、调整字段顺序都可能导致序列化失败，仅修改字段名称不会失败 | 差   | 小       |


### 对象引用关系序列化
对象直接存在引用关系，当引用一个已经序列化过的对象时，不需要重复序列化该对象。Light-serialize将序列化的根对象索引序号为"0"，下一个被序列化的对象
的索引序号加1，当序列化一个已经序列化过的对象时，序列化该关系索引值即可。在反序列化时一定要按照序列化时索引的顺序，反序列化即可。


### 类型引用序列化
当一个对象类型已经序列化时，如一个自定义的类A，如果该类未注册且第一次序列化，会使用类名进行序列化，同时将类型索引加1，作为该类型的索引，
下一次再序列化同一个类型时，只需序列化该索引值即可。


### 字段名称引用序列化
当序列化策略是"Name"时，会按对象的属性名称进行序列化，如果该属性名称是第一次序列化，会将该属性名序列化，同时将字段名索引加1，作为该属性名的索引，
下一次再序列化同样的属性名称时，只需序列化该索引值即可。


## Serializers
Light-serialize提供了Java常用的类序列化器，如基础类型、数组类型、枚举、代理、正则表达式、时间、lambda表达式、异常、集合等。当不满足要求时，
可以注册自定义序列化器来满足需求。


### ObjectSerializer
当一个对象没有对应的序列化器，会使用ObjectSerializer进行序列化，ObjectSerializer会根据序列化的策略按属性的名称、顺序、或者严格匹配进行序列化。

### 自定义序列化Serializer
当Light-serialize内置的序列化器不能满足要求时，可以注册自定义的Serializer来满足需求。
DefaultSerializerFactory.register序列化器，自定义的序列化器需要实现Serializer类的read和write方法。


# instantiator
Light-serialize在反序列化时并不要求类提供无参构造函数，默认使用"UnSafeInstantiator"构造实例，UnSafeInstantiator使用JDK的Unsafe构造实例。


# 最佳实战

+ 选择合适的序列化策略

  Light-serialize提供了多种序列化策略，需要根据自己的场景选择Name、Order、Exact。
  
+ Buffer复用，避免内存拷贝，自定义Buffer实现

  如果需要对一个对象进行多次的序列化和反序列化，尽量复用Buffer，可避免内存的重复拷贝，必要时实现自定义的Buffer实现。

+ 注册常用的序列化器，减小大小

  在一些场景中需要对一些常用的类进行序列化，如心跳、rpc请求等，可以对其进行注册，注册后会使用一个long值进行序列化，
  可减小序列化的字节大小，注意序列化方和反序列化方都要注册，否则会序列化失败。


   
# 基于netty封装0拷贝的Buffer实现RPC
封装netty的ByteBuf，在Rpc调用过程中,Rpc请求和响应对象的二进制流直接写入ByteBuf，不需要拷贝数据。
参考：light-serialize-example中的netty部分。启动RpcApplication即可。

