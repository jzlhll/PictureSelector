> fork from https://github.com/buhuiming/PictureSelector
> fork from https://github.com/LuckSiege/PictureSelector

# PictureSelector
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

xxx

