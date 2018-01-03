# PullUpDownRefresh

## Using PullUpDownRefresh in your application

If you are building with Gradle, simply add the following line to the `dependencies` section of your `build.gradle` file:

```
implementation 'com.soli.refresh:pull_up_down:0.0.2'
```

俗话说，前人栽树后人乘凉，我是在[android-Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh)下拉基础上，借用[CommonPullToRefresh](https://github.com/Chanven/CommonPullToRefresh)实现的思想，在此基础上，结合自己在实际项目中的经验，总结出来的一个东西。好用，扩展强！

#同时具有下拉刷新和自动预加载
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.soli.pullupdownrefresh.PullRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/test_list_view_frame"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/test_list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white" />

</com.soli.pullupdownrefresh.PullRefreshLayout>
```
分页加载最好设置pagesize，这样更好的实现各种情况下的分页
```
refreshLayout.setPageSize(pageSize);
```
设置监听回调
```
refreshLayout.setRefreshListener(new PullRefreshLayout.onRefrshListener() {
            @Override
            public void onPullDownRefresh() {
                
            }

            @Override
            public void onPullupRefresh(boolean actionFromClick) {

            }
        });
```

#单个listview上实现自动预加载更多的实现
比如要在ListView GridView(用GridViewWithHeaderAndFooter) 或RecyclerView上实现自动预加载的效果，直接用ListLoadMoreAction就行了，如：
```
  loadMoreAction = new ListLoadMoreAction();
  loadMoreAction.setPageSize(pageSize);
  
  loadMoreAction.attachToListFor(mListView, actionFromClick -> {
             //实现自动加载跟多的逻辑
  });
```

## 还没有弄得问题，空了弄

*  RecyclerView下的GridLayoutManager下，加载跟多的footview显示还有问题，不过这个也简单，只是没有统一写进去



 具体参看Demo
 

