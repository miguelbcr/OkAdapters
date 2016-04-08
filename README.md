# OkAdapters
Wrappers for Android adapters to simply its api at a minimum.   

## Setup
Add OkAdapter dependency to project level build.gradle.

```gradle
dependencies {
    compile 'com.github.FuckBoilerplate:OkAdapters:0.0.2'
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
 
 public class YourModelViewGroup extends FrameLayout implements BindView.Binder<YourModel> {
 
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
 
  Now instantiate [OkRecyclerViewAdapter](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/recycler_view/OkRecyclerViewAdapter.java) using the previous `BindView.Binder` implementation class and use it as a normal `adapter`.

 ```java
 
 OkRecyclerViewAdapter<YourModel, YourModelViewGroup> adapter = new OkRecyclerViewAdapter<YourModel, YourModelViewGroup>() {
     @Override protected YourModelViewGroup onCreateItemView(ViewGroup parent, int viewType) {
         return new YourModelViewGroup(parent.getContext());
     }
 };
 
 recyclerView.setAdapter(adapter);
 
  ```
  
## Spinner
Create a class which extends from any Android `ViewGroup` and implements [OkSpinnerAdapter.Binder](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/spinner/OkSpinnerAdapter.java). This approach allows to encapsulate the binding between the data and the `view`.
 
 ```java
  
public class YourModelViewGroup extends FrameLayout implements OkSpinnerAdapter.Binder<YourModel> {

    public YourModelViewGroup(Context context) {
        super(context);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.your_model_view_group, this, true);
        ButterKnife.bind(this, view);
    }

    @Bind(R.id.tv_value) TextView tv_value;

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
  
  Now instantiate [OkSpinnerAdapter](https://github.com/FuckBoilerplate/OkAdapters/blob/master/library/src/main/java/library/spinner/OkSpinnerAdapter.java) using the previous `OkSpinnerAdapter.Binder` implementation class and use it as a normal `adapter`.

 ```java 

    List<YourModel> items = getItems();
 
    OkSpinnerAdapter<YourModel, YourModelViewGroup> adapter = new OkSpinnerAdapter<YourModel, YourModelViewGroup>(context, items) {
        @Override
        public YourModelViewGroup inflateView() {
            return new YourModelViewGroup(context);
        }
    };
    
    spinner.setAdapter(adapter);
 
  ```
  
[Reference](https://github.com/FuckBoilerplate/OkAdapters/tree/master/app/src/main/java/app/recycler_view) to a complete example.  
