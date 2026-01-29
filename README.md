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
  // PictureSelector basic (Necessary) 我修改了两个模块，按需使用
  implementation 'io.github.jzlhll:pictureselector:3.16.6'

  // uCrop library (Not necessary)
  implementation 'io.github.jzlhll:pictureselector_urop:3.16.6'
    
  // image compress library (Not necessary)
  implementation 'com.github.buhuiming.PictureSelector:compress:3.15.1'

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

其他建议：

- 不使用白色主题；只使用默认主题或者微信主题；后续我会收缩代码，不提供外部额外定制。



### 发布历史

#### 3.16.6

修正全选按钮的显示问题。


#### 3.16.4

- 沉浸式调整：

  > 完全沉浸式布局，使用edgeToEdge正确处理statusBar和navBar；调整默认主题背景色调，避免某些手机statusBar颜色乱变

- 界面优化和bug修复：

  > 修正ConstraintLayout布局导致的位置错误
  >
  > 重构titleBar/previewBar/BottomNavBar，简化界面，不允许定制
  >
  > 修改选择图标原型为矩形，以适配个数100以上显示不好看的问题
  >
  > 微信UI移除总选项，因为现在的微信也没有总数
  >
  > 修复preview隐藏系统bars时退出未还原的问题
  > 修复按钮设置不合理的问题
  > 修复ConstraintLayout布局导致的位置错误

- 新功能：

  > 全选模式：使用setMaxSelectNum(Integer.MAX_VALUE)来激活

- 简化：

  > 移除外部Fragment preview移除cancelButton，简化配置项
  >
  > 移除外部设置Fragment preview逻辑
  >
  > 移除所有动态调整margin和left左右

  


### TODO 简化其他逻辑

我开发的原则是，遵循最简化开发。
不应该给开发者提供过多选择。虽然定制化强大，但是开源库需要维护的东西过于庞大，尤其是对于UIStyle而言。

目前有2个问题：

Style
   我会完全移除主题定制相关的API。
   库的使用者以resource overlay（人话：主模块覆盖子模块）的方式自动实现，而不是通过代码传递参数来修改。压根不需要原来的inject layout的方式。



=====================分割线========================================

==================================================================



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

