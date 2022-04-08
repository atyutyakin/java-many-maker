package pro.alxerxc.menuMaker.entity;

public interface Persistable<ID> {

    ID getId();

    default boolean hasId() {
        return getId() != null;
    }
}
