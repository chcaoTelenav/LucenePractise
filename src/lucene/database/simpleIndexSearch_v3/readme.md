Created by chcao on 5/8/2017.
 
这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
然后当进行search操作的时候，其实就是对那些index进行了查找。

这是对数据库中数据进行搜索，其实是搜索了之前创造的index文件

目前的查找还都是精确查找

v3

修改部分：
1）增加各输入校验
    解决：
        增加一些输入空值的校验等。
        
2）重复创建index会有问题，需要fix
    改成，检查index是否存在。存在，是否覆盖（删除-》添加）；不覆盖，直接过
    看了代码。
    《IndexWriter里面默认的属性就是 CREATE_OR_APPEND所以不需要显示的去写出来》
    问题：
        发现是多次创建index后，index文件里面term的frequency一直在增加.导致搜索时候，反复index的数据都被搜索出来。需要想办法避这点。
    解决办法1：
        将IndexWriterConfig.OpenMode改为 CREATE 每次index都删除老的，创建新的。（感觉不是很好，看看是不是有优化方案）
        
3）支持多field查询
    解决：
        使用了MultiFieldQueryParser解决。
