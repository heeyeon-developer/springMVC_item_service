package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor    //final 이 붙은 객체를 이용하여 주입되는 생성자를 만들어줌
public class BasicItemController {

    private final ItemRepository itemRepository;
//    @Autowired    //생성자가 1개만 만들어진다고 확정되면 생략 가능
//    public BasicItemController(ItemRepository itemRepository){//@RequiredArgsConstructor 애노테이션으로 인해 생략 가능
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("ItemA", 10000, 10));
        itemRepository.save(new Item("ItemB", 20000, 20));
        itemRepository.save(new Item("ItemC", 30000, 30));
    }
}
