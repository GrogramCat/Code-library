// 摘抄于 enjoy风铃 - 值得收藏的 ViewHolder 工具类实现 - http://www.cnblogs.com/net168/p/4477925.html
// BaseAdapter一般实现写法
@Override
public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if(convertView == null) {
        convertView = inflater.inflate(R.layout.lv_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.name = (TextView)convertView.findViewById(R.id.tv_name);
        viewHolder.age = (TextView)convertView.findViewById(R.id.tv_age);
        convertView.setTag(viewHolder)
    } else {
        viewHolder = (ViewHolder)convertView.getTag();
    }
    Person person = (Person)getItem(position);
    viewHolder.name.setText(person.getName());
    viewHolder.age.setText(person.getAge());
    return convertView;
}

class ViewHolder {
    public TextView name;
    public TextView age;
}

/* ----------------------------------华丽丽的分界线------------------------------------- */

// ViewHolder工具类的实现
static class ViewHolder {
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>)view.getTag();
        if(viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if(childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T)childView;
    }
}
// 1)ViewHolder依赖View的Tag存放,以一个SparseArray集合存放
// 2)判断View里面的Tag是否存在viewHolder,不存在,则设置一个新的
// 3)在viewHolder寻找View的索引,如果没有,就通过findViewById获得,put进viewHolder中,并return,如果存在直接使用

// 在BaseAdapter中的使用
@Override
public View getView(int position, View ConvertView, ViewGroup parent) {
    if(convertView = null) {
        convertView = inflater.inflate(R.layout.lv_item, parent, false);
    }
    TextView name = Tools.ViewHolder.get(convertView, R.id.tv_name);
    TextView age = Tools.ViewHolder.get(convertView, R.id.tv_age);

    Person person = (Person)getItem(position);
    name.setText(person.getName());
    age.setText(person.getAge());
    
    return convertView;
}
