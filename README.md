> fork from https://github.com/buhuiming/PictureSelector
> 
> fork from https://github.com/LuckSiege/PictureSelector
>
我现在也fork了项目，做了修改：
https://github.com/jzlhll/PictureSelector

# PictureSelector

```groovy
repositories {
    //...
  mavenCentral()
}

dependencies {
  // PictureSelector basic (Necessary) 我只修改了该模块，其他暂时不编译，不影响
  implementation 'io.github.jzlhll:pictureselector:3.16.2'

  // image compress library (Not necessary)
  implementation 'com.github.buhuiming.PictureSelector:compress:3.15.1'

  // uCrop library (Not necessary)
  implementation 'com.github.buhuiming.PictureSelector:ucrop:3.15.1'

  // simple camerax library (Not necessary)
  implementation 'com.github.buhuiming.PictureSelector:camerax:3.15.1'
}
```

现在对于非普通应用强烈推荐：
- 选择图片和视频：使用`ActivityResultContract` + `PickVisualMediaRequest`，又简单又轻松，
而且更重要的是，你的应用压根不需要申请任何权限。要知道，管理细分三个权限，和用户选择区域权限，以及android12的write storage权限真的很烦。
  - 唯一缺点就是上限100个文件。主要原因可能是跨进程Intent的binder上限1MB-8k。

- 选择audio：通过`ActivityResultContracts.GetMultipleContents()`来实现。

所以我为什么要clone这个项目？

## 我做了什么修改
### 全选功能
~原项目没有这个功能，已经没有存在的必要。~

**这也是android发展到现在，PictureSelector库存在的唯一意义。用于相册类应用和系统程序集成。**

当设置参数：
```java
//一定不要设置该参数，目的是为了拷贝到本地目录
//.setSandboxFileEngine(new MeSandboxFileEngine())
//也不建议设置该参数，目的是为了进行压缩
//.setCompressEngine(getCompressFileEngine())

//设置maxSelectNum为int最大值，切换成全选模式
.setMaxSelectNum(Integer.MAX_VALUE)
```
右上角就有全选模式和取消全选功能，用于做大批量相册导入导出的全选操作。

### 资源方式导致显示问题修正
constraintLayout内部一堆wrap高度加相互约束，高度等参数又是动态设定，导致无法正确显示。

### TODO 简化沉浸式和主题

我开发的原则是，遵循最简化开发。
不应该给开发者提供过多选择。虽然定制化强大，但是开源库需要维护的东西过于庞大，尤其是对于UIStyle而言。

目前有2个问题：
1. 沉浸式
   这里面的沉浸式代码太多太乱了，而且仍然存在navigationBar和constraintLayout等导致显示黑屏。我计划简化，使用edgeToEdge来实现，对于preview的时候做适配动态显示systemBars即可。

2. Style
   我会完全移除主题定制相关的API。
   库的使用者以resource overlay（人话：主模块覆盖子模块）的方式自动实现，而不是通过代码传递参数来修改。压根不需要原来的inject layout的方式。

## 背景
android的图片选择器的发展故事是这样的：

### 蛮荒时代

该有的权限申请一下即可。应用内显示。

在这个阶段，`PictureSelector`发展壮大，几乎成为了开源图片选择器的唯一选择。

主要就是查询相册，将`content://media/external/images/media/12345`, `content://media/external/video/media/12346` 等图片，视频，音频做显示，库内部做了极多的参数配置，极多的Engine提供给开发者去定制比如图片加载器，播放器加载器，还考虑异步加载，快速加载，样式等方方面面。

### android10～android13阶段

应用无法通过`WRITE_EXTERNAL_STORAGE`权限，随意往外置目录写入；

- 需要使用`MediaStore.Images`/`MediaStore.Video`/`MediaStore.Audio`来写回系统的相册；

- 或者`android:requestLegacyExternalStorage="true"` (targetSdkVersion=29) 仅限android10。而且api 32，已经彻底移除；
- 或者`ActivityResultContracts.OpenDocumentTree()`来做目录选择授权，但是可能需要二次申请。

图片选择框架，需要追加一堆权限申请，并且逐渐抛弃/兼容`WRITE_EXTERNAL_STORAGE`。

### android13阶段

Google隐私权限继续加强，做了细分权限。

```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
<uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
```

这个时候开始，已经不推荐使用第三方的图库选择框架了。推荐使用`ActivityResultContract` + `PickVisualMediaRequest` 后续简称`Picker`。

对于非相册类app，googlePlay推荐，使用`ActivityResultContract` + `PickVisualMediaRequest`。

会弹出一个好看的底部弹窗选择器。如果android12系统不支持的话，会调用到一个稍微难看的文件浏览器的界面，也算能往下兼容（如果是海外GMS框架下，会有兼容的picker支持）。

### android14阶段

Google又加强了一把，弄了一个选择图册的时候，弹出一个先勾选临时的相册，再传递给应用去pick：

```kotlin
<uses-permission android:name="android.permission.READ_MEDIA_VISUAL_USER_SELECTED" />
```

### android15阶段

需要兼容沉浸式。其实在android13-14也在逐步加强，到android15/16强制。

### 现在的情况

原项目`LuckSiege/PictureSelector` 最后的版本`v3.11.2 `定格在：Dec 17, 2023。
好在对于android14的细分权限也已经支持。没有对于android15的沉浸式加强。

`buhuiming/PictureSelector` 进行了fork主要修正android15的沉浸式。

现在对于非普通应用，使用`ActivityResultContract` + `PickVisualMediaRequest`，又简单又轻松，而且重要的是，你的应用压根不需要任何权限。


