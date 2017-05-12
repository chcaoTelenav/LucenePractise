Created by chcao on 5/8/2017.
 
这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
然后当进行search操作的时候，其实就是对那些index进行了查找。

这是对数据库中数据进行搜索，其实是搜索了之前创造的index文件

==============
V5目前提供的功能有：（针对students表）
1. 手动选择要查询的字段，进行 ( **_精确/前缀_** ) 两种模式的查找
2. students表的所有字段，除了birthday使用了（自定义的MyAnalyzer）之外，其余字段都使用的StandardAnalyzer


===========
v5 — 任务
整理并完成了V4的预期功能：
1）数据库表添加多个不同类型的columns
2）自定义几个Query，进行比较复杂的查询操作，即使用Query操作，而不是使用QueryParser。
    （1）可以指定对单独Field进行查询操作----In student index, find documents whose name called "Mary", query based on "name" field only.
    （2）自定义Analyzer：支持Prefix----In student index, find documents whose phone number start with "123".
3）自定义一个Analyzer，可以通过不同format的时间去找到正确的时间。需要识别YYYYMMDD" and "YYYY/MM/DD". 

======
V5 要解决的问题：

1） 有一个问题是 对于 age（int）进行精确查询时发现没有结果。检查后发现，原来是TermQuery不支持int对象。 而4.X版本之后又没有单独搜索一个数字的query存在，所以只能使用 数字范围搜索 ==》 NumericRangeQuery.newIntRange() 去实现这个数字功能
    **解决方案：**
      使用 NumericRangeQuery.newIntRange()。
       （搞定）
        ！！！！这里有个Bug，就是当选择 “age && prefix”的时候，使用的还是前缀搜索，是搜不到数字的。这里需要改成组合查询，即将 numericRangeQuery和prefix组合起来使用。。暂时没改。有空再研究。。。
        
2) 搜索时候发现 “sing” 在 “personalInfo”中无法搜索到。查看index后发现，单词并没有变成原词，而是依然带有各种时态的存在index中。
    **解决方法**
        **Index**时候使用了自定义的Analyzer，里面使用了额PorterStemFilter。
        **Search**的时候使用了 PorterStemer.setCurrent()和 PorterStemer.stem()方法，使得查找关键字也被还原成原词。
        
3) 增加了对于菜单输入值的校验。如age，如果输入 “f” 会报错
    **解决方法**
        针对 int的field，进行了是否是数字的校验。 一些菜单的跳转还不是很灵活，不过有空再说