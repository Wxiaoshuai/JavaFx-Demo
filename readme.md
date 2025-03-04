## 运行指令
```
./mvnw clean javafx:run
```

## 生成app指令
```
./mvnw clean javafx:jlink --add-modules ALL-MODULE-PATH 
```

## 打包指令
```
jlink --add-modules ALL-MODULE-PATH --module-path D:\home\javaFx\demo\target\demo-1.0-SNAPSHOT.jar --output D:\home\javaFx\demo\app --launcher myapp=com.example.demo.HelloApplication

jlink --add-modules D:\home\javaFx\demo\poi-jar\poi-5.2.3.jar --module-path D:\home\javaFx\demo\target\demo-1.0-SNAPSHOT.jar --output D:\home\javaFx\demo\app --launcher myapp=com.example.demo.HelloApplication


jpackage --name test --type app-word -m com.example.demo.HelloApplication --runtime

jpackage --input target --main-jar demo-1.0-SNAPSHOT.jar --main-class com.example.demo.HelloApplication `
--add-modules ALL-MODULE-PATH `
--name MyApp --dest output --type exe
```

## jar命令
```
java -jar demo-1.0-SNAPSHOT.jar  

javac --module-path C:\java\javafx-sdk-21.0.6\lib --add-modules javafx.controls,javafx.fxml -d out $(find src -name "*.java")

jdeps --multi-release 21 --ignore-missing-deps --generate-module-info . *.jar

jdeps --multi-release 21 --ignore-missing-deps --generate-module-info jars . *.jar

javac --patch-module commons.math3=commons-math3-3.6.1.jar D:\home\javaFx\untitled2\commons.math3\versions\21\module-info.java

jar uf commons-math3-3.6.1.jar -C D:\home\javaFx\untitled2\commons.math3\versions\21\module-info.class



jar xf C:\Users\15622\.m2\repository\com\zaxxer\SparseBitSet\1.3\SparseBitSet-1.3.jar

javac --module-path D:\home\javaFx\demo\dependencies -d . module-info.java

jar --create --file SparseBitSet-1.3-modular.jar -C . .


jar xf C:\Users\15622\.m2\repository\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar

javac --module-path D:\home\javaFx\demo\commons-math3 -d . module-info.java

jar --create --file commons-math3-3.6.1.jar -C . .


jar xf C:/Users/15622/.m2/repository/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar

javac --module-path D:\home\javaFx\demo\commons-collections4 -d . module-info.java

jar --create --file commons-collections4-4.4.jar -C . .


jar xf C:\Users\15622\.m2\repository\org\apache\poi\poi\5.3.0\poi-5.3.0.jar

javac --module-path D:\home\javaFx\demo\poi -d . module-info.java
javac --module-path D:\home\javaFx\demo\poi: -d . module-info.java

jar --create --file poi-5.3.0.jar -C . .

jar --describe-module --file poi-5.2.3.jar



jdeps --ignore-missing-deps --generate-module-info . demo-1.0-SNAPSHOT.jar

jdeps --module-path C:\Users\15622\.m2\repository\org\apache\poi\poi-ooxml\5.3.0\poi-ooxml-5.2.3.jar --ignore-missing-deps --generate-module-info . demo-1.0-SNAPSHOT.jar

```