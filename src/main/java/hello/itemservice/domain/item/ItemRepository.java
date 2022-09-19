package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();//static
    //실무에서 멀티스레드를 사용할때는 ConcurrentHashMap<>() 을 사용해야 함, 현재 저장소가 싱글톤으로 하나의 저장소에 동시 접근할 수가 있는 구조이기 때문에 순서 중요
    private static long sequence = 0L;//static

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());//한번 감싸서 나가면 store에 영향을 주지 않기때문에 그리고 타입 맞추기 위해
    }

    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }

}
