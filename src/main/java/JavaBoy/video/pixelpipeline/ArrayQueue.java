package JavaBoy.video.pixelpipeline;


public class ArrayQueue <T>  {
    private final Object[] data;
    private final int capacity;
    private int size = 0;
    private int head =0;
    private  int tail = head;

    public ArrayQueue(int capacity){
        this.capacity = capacity;
        this.data = new Object[capacity];
    }

    public T poll(){
        --size;
            T item = (T) data[head];
       head = (++head) % capacity;

        return item;
    }


    public T peek (){
        return (T)data[head];
    }
    public void clear(){
        this.head = this.tail = this.size = 0;
    }
    public void add(T item){
       ++size;
       data[tail] = item;
       tail = (++tail) % capacity;
    }

    public int size(){
       return  this.size;
    }

    public T getAt(int index){
            return (T)data[(head + index) % capacity];
    }

    public void setAt(int index, T value){
         data[(head + index) % capacity] = value;
    }


}
