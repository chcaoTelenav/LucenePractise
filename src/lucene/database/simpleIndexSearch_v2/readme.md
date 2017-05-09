Created by chcao on 5/8/2017.
 
这个是模拟项目。即数据原先在数据库中，然后对这些数据建立index，存到文件中。
然后当进行search操作的时候，其实就是对那些index进行了查找。

这是对数据库中数据进行搜索，其实是搜索了之前创造的index文件

v2 版本
1）修改了源代码，将indexpath参数和search参数提取出来
2）建立一个 call的类，将search和index集成进去
3）添加一个命令行形式的交互ui
