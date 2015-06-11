package Game;

public interface Game {
    public Iterable<GameObject> getGameObjects();
    public Iterable<GameObject> getBackgroundObjects();
    public void add(GameObject obj);
}
