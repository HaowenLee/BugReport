# Android 开发环境下崩溃信息展示

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![JCenter](https://img.shields.io/badge/%20JCenter%20-0.0.13-5bc0de.svg)](https://bintray.com/haowen/maven/bugreport/_latestVersion)
[![JitPack](https://jitpack.io/v/HaowenLee/BugReport.svg)](https://jitpack.io/#HaowenLee/BugReport)
[![MinSdk](https://img.shields.io/badge/%20MinSdk%20-%2014+%20-f0ad4e.svg)](https://android-arsenal.com/api?level=14)

### 效果图

<img src="https://github.com/HaowenLee/BugReport/blob/master/arts/bug_report.png" width="300" alt="BugReport"/>


#### 步骤 1. 在module的build.gradle添加依赖项

```
dependencies {
        implementation 'com.haowen.bugreport:bugreport:0.0.13'
}
```

#### 步骤 2. 在自定义的Application里初始化

```
CrashHandler.getInstance().init(this);
```

#### todo list

- 以文件形式存储Crash信息（html格式）
- Log日志存储（html格式）
