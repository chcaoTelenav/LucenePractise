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
    （2）自定义Analyzer：支持Prefix----In student index, find documents whose phone number start with "123".
    
3）自定义一个Analyzer，可以通过不同format的时间去找到正确的时间。需要识别YYYYMMDD" and "YYYY/MM/DD". 
    
4）使用IKAnalyzer进行中文分词的查找使用。