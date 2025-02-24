## 运行指令
```
./mvnw clean javafx:run
```

## 生成app指令
```
./mvnw clean javafx:jlink
```

## 打包指令
```
jpackage --name test --type app-word -m com.example.demo.HelloApplication --runtime

jpackage --input target --main-jar myapp.jar --main-class com.example.demo.HelloApplication --name MyApp --dest output --type exe --runtime-image C:\java\javafx-sdk-21.0.6
```