package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam Integer price,
                       @RequestParam Integer quantity,
                       Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item",item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
//@ModelAttribute 가 하는 일은 자동으로 객체를 생성해주고 생성한 객체가 뷰에서 쓰일겻이기 때문에 model.addAttribute() 도 진행해줌

        itemRepository.save(item);
//        model.addAttribute("item",item);//@ModelAttribuet() 사용시 생략 가능,Model 도 생략 가능

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){//@ModelAttribute(name) 을 생략하면 클래스의 이름 첫글자를 소문자로 바꾸어 이름으로 명시됨 Item -> item
        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item){//@ModelAttribute 생략 가능하여 생략해도 동일하게 작동!
        itemRepository.save(item);
        return "basic/item";
    }

    //PRG 패턴으로 수정한 최종 결과 -> 뷰 템플릿을 보여주는 것이 아닌 주소를 변경하는 redirect 를 사용
//    @PostMapping("/add")
    public String addItemV5(Item item){//@ModelAttribute 생략 가능하여 생략해도 동일하게 작동!
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){//@ModelAttribute 생략 가능하여 생략해도 동일하게 작동!
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());//savedItem.getId 가 치환되어 itemId 로 저장됨, url 인코딩도 이 방식으로 하여 해결 가능
        redirectAttributes.addAttribute("status",true);// 쿼리파라미터 형식으로 들어가게 됨
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editform(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
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
