package zinterfaces;

@FunctionalInterface
public interface iMenuAction extends Runnable {
    @Override
    void run();
}