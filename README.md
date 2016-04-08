# OkAdapter
Wrappers for Android adapters to simply its api at a minimum.   

## Setup
Add OkAdapter dependency to project level build.gradle.

```gradle
dependencies {
    compile 'com.github.FuckBoilerplate:OkAdapter:0.0.1'
}
```

Add jitpack repository to root level build.gradle.

```gradle
allprojects {
    repositories {
        //..
        maven { url "https://jitpack.io" }
    }
}

```

## Usage

OkAdapters provides several adapters to deal with specific Android `views`.

## RecyclerView
Create a class which extends from any Android `ViewGroup` and implements [BindView.Binder](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/recycler_view/BindView.java). This approach allows to encapsulate the binding between the data and the `view`.
 
 ```java
 
 public class ItemViewGroup extends FrameLayout implements BindView.Binder<YourModel> {
 
     public ItemViewGroup(Context context) {
         super(context);
 
         View view = LayoutInflater.from(getContext()).inflate(R.layout.your_model_view_group, this, true);
         ButterKnife.bind(this, view);
     }
  
     @Bind(R.id.tv_value) TextView tv_value;

     @Override public void bind(YourModel model, int position) {
        tv_value.setText(model.getValue());
     }
     
 }
 
 ```
 
  Now instantiate [OkRecyclerViewAdapter](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/recycler_view/OkRecyclerViewAdapter.java) using the previous `BindView.Binder` implementation class and use it as a normal `adapter`.

 ```java
 
 OkRecyclerViewAdapter<Item, ItemViewGroup> adapter = new OkRecyclerViewAdapter<Item, ItemViewGroup>() {
     @Override protected ItemViewGroup onCreateItemView(ViewGroup parent, int viewType) {
         return new ItemViewGroup(parent.getContext());
     }
 };
 
 recyclerView.setAdapter(adapter);
 
  ```
[Reference](https://github.com/FuckBoilerplate/OkAdapters/tree/master/app/src/main/java/app/recycler_view) to a complete example.  
