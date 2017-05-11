Created by chcao on 5/8/2017.
 
这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
然后当进行search操作的时候，其实就是对那些index进行了查找。

这是对数据库中数据进行搜索，其实是搜索了之前创造的index文件

目前的查找还都是精确查找

v4 — 任务

修改部分：

1）数据库表添加多个不同类型的columns
    **解决：**
        添加students表，有int，date和string属性。

2）自定义几个Query，进行比较复杂的查询操作，即使用Query操作，而不是使用QueryParser。
    （1）可以指定对单独Field进行查询操作----In student index, find documents whose name called "Mary", query based on "name" field only.
        **简单解决：大小写不敏感**
    （2）自定义Analyzer：支持Prefix----In student index, find documents whose phone number start with "123".
        **简单解决：目前只测了电话**
    
3）自定义一个Analyzer，可以通过不同format的时间去找到正确的时间。需要识别YYYYMMDD" and "YYYY/MM/DD". 
        **简单解决1**
            写了个方法，直接在数据传入之前就对其进行了格式化操作。都转换成yyyyMMdd的形式。
            但是有个问题，就是这个功能实现了，但是并没有自定义任何的Analyzer。
        **简单解决2**
            自定义了一个Analyzer，里面使用了
            自定义的Tokenizer和
            自定义的Tokenfilter来格式化传入的时间。将其转变成yyyyMMdd的形式，并且以字符串形式存入index中，而不是之前使用的long的形式。
            



===========
目前需要修复问题：

1. search时候输入大写字母，无法与index中数据匹配，即输入Mary，找不到人。需要查看一下Analyzer
    问题解决：
        想偏了，直接在java中将传入的keyWords转成小写，就可以解决这个问题

2. 菜单需要改进，field不需要手输，改为序号
    问题解决：
        用map实现

3.