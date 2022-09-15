## RoundCircleImageView

可以支持圆角与圆形裁剪的ImageView。基于BitmapShader实现，无需配置硬件加速，无兼容性问题。

依赖方法：
```
mavenCentral()  //默认的Maven仓库

implementation "io.github.liukai2530533:round_circle_imageview:1.0.1"
```

#### 如何使用：

自定义属性如下：
```xml
<attr name="isCircle" format="boolean" />
<attr name="round_background_color" format="color" />
<attr name="round_background_drawable" format="reference" />
<attr name="round_radius" format="dimension" />
<attr name="topLeft" format="dimension" />
<attr name="topRight" format="dimension" />
<attr name="bottomRight" format="dimension" />
<attr name="bottomLeft" format="dimension" />
```

在xml中使用如下：
```xml
<com.newki.circle_round.RoundCircleImageView
    android:id="@+id/iv_custom_round"
    android:layout_width="@dimen/d_150dp"
    android:layout_height="@dimen/d_150dp"
    android:layout_marginTop="@dimen/d_10dp"
    app:isCircle="false"
    app:round_background_drawable="@drawable/shape_blue"
    app:round_background_color="@color/gray"
    app:round_radius="@dimen/d_20dp" />

<com.newki.circle_round.RoundCircleImageView
android:id="@+id/iv_custom_round2"
android:layout_width="@dimen/d_150dp"
android:layout_height="@dimen/d_150dp"
android:layout_marginTop="@dimen/d_10dp"
app:bottomLeft="@dimen/d_20dp"
app:bottomRight="@dimen/d_40dp"
app:isCircle="false"
app:round_background_drawable="@drawable/chengxiao"
app:topLeft="@dimen/d_40dp"
app:topRight="@dimen/d_20dp" />

<com.newki.circle_round.RoundCircleImageView
android:id="@+id/iv_custom_round3"
android:layout_width="@dimen/d_150dp"
android:layout_height="@dimen/d_150dp"
android:layout_marginTop="@dimen/d_10dp"
app:bottomLeft="@dimen/d_20dp"
app:bottomRight="@dimen/d_40dp"
app:isCircle="false"
app:round_background_color="@color/gray"
app:topLeft="@dimen/d_40dp"
app:topRight="@dimen/d_20dp" />
```

效果如下：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bef92376db3e42769ba5cac3355ffcdf~tplv-k3u1fbpfcp-zoom-1.image)


同时也支持代码中设置：

```kotlin
findViewById<RoundCircleImageView>(R.id.iv_custom_round).background = drawable(R.drawable.shape_blue)

findViewById<RoundCircleImageView>(R.id.iv_custom_round2).background = drawable(R.drawable.chengxiao)
```

当然，使用图片加载框架也可以直接加载网图，或者直接手动设置原生的Bitmap Background都可以（内部已经重写方法）：

```kotlin
findViewById<RoundCircleImageView>(R.id.iv_custom_round).setRoundBackgroundColorResource(R.color.picture_color_blue)
findViewById<RoundCircleImageView>(R.id.iv_custom_round).extLoad(imgUrl, R.drawable.test_img_placeholder)

findViewById<RoundCircleImageView>(R.id.iv_custom_round2).setRoundBackgroundColorResource(R.color.picture_color_blue)
findViewById<RoundCircleImageView>(R.id.iv_custom_round2).extLoad("123", R.drawable.test_img_placeholder)
```

效果：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/39289657c6ed46d8ad3c28e0cf19519e~tplv-k3u1fbpfcp-zoom-1.image)


具体的实现可以参考我的博客：

https://juejin.cn/post/7143410101951397919