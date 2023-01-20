package al.kr.mdfactory.connection;

@FunctionalInterface
public interface Callback<T> {
    void accept(int code, T arg);
}
