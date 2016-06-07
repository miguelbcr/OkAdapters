# OkAdapters
Wrappers for Android adapters to simply its api at a minimum.   

## Setup
Add OkAdapter dependency to project level build.gradle.

```gradle
dependencies {
    compile 'com.github.FuckBoilerplate:OkAdapters:0.1.1'
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
Create a class which extends from any Android `ViewGroup` and implements `OkRecyclerViewAdapter.Binder`. This approach allows to encapsulate the binding between the data and the `view`.
 
```java
 public class YourModelViewGroup extends FrameLayout implements OkRecyclerViewAdapter.Binder<YourModel> {
 
     public YourModelViewGroup(Context context) {
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
 
Now instantiate [OkRecyclerViewAdapter](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/recycler_view/OkRecyclerViewAdapter.java) using the previous `OkRecyclerViewAdapter.Binder` implementation class and use it as a normal `adapter`.

```java
 OkRecyclerViewAdapter<YourModel, YourModelViewGroup> adapter = new OkRecyclerViewAdapter<YourModel, YourModelViewGroup>() {
     @Override protected YourModelViewGroup onCreateItemView(ViewGroup parent, int viewType) {
         return new YourModelViewGroup(parent.getContext());
     }
 };
 
 recyclerView.setAdapter(adapter);
 
```
  
### RecyclerView pagination.

`OkRecyclerViewAdapter` supports a reactive pagination. In order to use this feature, you need to call `setRxPager` from the adapter, supplying a valid reference to the layout which will be used as the loading row when requesting successive items. As long as an implementation of the interface `LoaderPager`, which exposes the last visible item and request an instance of the loader `observable` for retrieving the data. 

```java
adapter.setRxPager(R.layout.loading_pager, new RxPager.LoaderPager<YourModel>() {
    @Override public Observable<List<YourModel>> onNextPage(YourModel lastItem) {
        return getItemsObservable(lastItem);
    }
});
```

It is also possible to call `resetPager`, supplying an observable as the data source, in order to restart the pagination using another loader source that the one used for the pagination.
  
```java
adapter.resetPager(getFreshItemsObservable(null));
```
  
## Spinner
Create a class which extends from any Android `ViewGroup` and implements `OkSpinnerAdapter.Binder` and `OkSpinnerAdapter.BinderDropDown` for the same `view` or for two different `views` which implement each interface separately. This approach allows to encapsulate the binding between the data and the `view`.
 
```java
public class YourModelViewGroup extends FrameLayout implements OkSpinnerAdapter.Binder<YourModel>, OkSpinnerAdapter.BinderDropDown<YourModel> {
    @Bind(R.id.tv_value) TextView tv_value;

    public YourModelViewGroup(Context context) {
        super(context);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.your_model_view_group, this, true);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindDropDownView(YourModel model, int position) {
        tv_value.setText(model.getValue());
    }

    @Override
    public void bindView(YourModel model, int position) {
        tv_value.setText(model.getValue());
    }
}  
```
  
  Now instantiate [OkSpinnerAdapter](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/spinner/OkSpinnerAdapter.java) using the previous `OkSpinnerAdapter.Binder` and `OkSpinnerAdapter.BinderDropDown` implementation class and use it as a normal `adapter`.

```java 
    List<YourModel> items = getItems();
 
    OkSpinnerAdapter<YourModel, YourModelViewGroup, YourModelViewGroup> adapter = new OkSpinnerAdapter<YourModel, YourModelViewGroup, YourModelViewGroup>(context, items) {
        @Override
        public YourModelViewGroup inflateView() {
            return new YourModelViewGroup(context);
        }

        @Override
        public YourModelViewGroup inflateDropDownView() {
            return new YourModelViewGroup(context);
        }
    };
    
    spinner.setAdapter(adapter);
 
```
  

## BaseAdapter
Create a class which extends from any Android `ViewGroup` and implements `OkBaseAdapter.Binder`. This approach allows to encapsulate the binding between the data and the `view`.
 
```java
 public class YourModelViewGroup extends FrameLayout implements OkBaseAdapter.Binder<YourModel> {
 
     public YourModelViewGroup(Context context) {
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
 
Now instantiate [OkBaseAdapter](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/base_adapter/OkBaseAdapter.java) using the previous `OkBaseAdapter.Binder` implementation class and use it as a normal `adapter`.

```java
OkBaseAdapter<Item, ItemViewGroup> adapter = new OkBaseAdapter<YourModel, YourModelViewGroup>() {
    @Override public YourModelViewGroup inflateView() {
        return new YourModelViewGroup(getContext());
    }
};
        
 stackView.setAdapter(adapter);
 
```
  
  
[Reference](https://github.com/FuckBoilerplate/OkAdapters/tree/master/app/src/main/java/app/stack_view) to a complete example.  
