set PATH=\WorkServer\Java\jdk\bin;%PATH%
java -classpath jskey.js.TrimScript.jar; jskey.js.TrimScript "UTF-8" "UTF-8" "./" "./../../tags/"
rem args参数说明
rem [0]读取文件字符集，默认"UTF-8"
rem [1]写入文件字符集，默认"UTF-8"
rem [2]js文件来源目录，默认"./"，以./开头的则指相对路径(如:["./source"],["./../source"])，其它为绝对路径
rem [3]js文件目的目录，默认"./mini/"，以./开头的则指相对路径(如:["./source/mini"],["./../source/mini"])，其它为绝对路径，不能来源目录相同，否则使用默认值
pause