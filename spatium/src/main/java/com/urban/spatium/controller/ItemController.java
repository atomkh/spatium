package com.urban.spatium.controller;

import java.util.List;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.urban.spatium.dto.Item;

import com.urban.spatium.dto.Store;
import com.urban.spatium.service.ItemService;
import com.urban.spatium.service.StoreService;


@Controller("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	@Autowired
	private StoreService storeService;
	
	@GetMapping("/item/seller/myItemInsert")
	public String myItemInsert(Model model, HttpSession session) {
		String storeId = (String) session.getAttribute("SID");
		String code = storeService.storeCodeGet(storeId);
		model.addAttribute("code", code);
		return "item/seller/myItemInsert";
	}
	
	@PostMapping("/item/seller/myItemInsert")
	public String myItemInsert(Item item, HttpSession session
								,@RequestParam(name = "code", required = false)int code) {
		
		System.out.println(item + "=========== 장비 넘어온 값 ============");
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(sessionId);
		item.setItemDetailUserId(sessionId);
		System.out.println(code + "포스트 맵핑 장비등록 스토어코드");
		item.setStoreDetailCode(code);
		String result = itemService.addItem(item);
		
		System.out.println(result);
		
		return "redirect:/user/myPage";
	}
	
	/* 메인 페이지에서 내 장비 조회 버튼 클릭시 들어오는 컨트롤러 */
	@GetMapping("/item/myItem")
	public String myItemList(Model model, HttpSession session, Item item) {
		
			String storeId = (String) session.getAttribute("SID");
			List<Item> itemList = itemService.myItemList(storeId);
			List<Item> itemCountList = itemService.myItemCountList(storeId);
			model.addAttribute("itemList", itemList);
			model.addAttribute("itemTotalLi", itemCountList);
			
		return "item/seller/myItem";
	}
	
	//장비구입내역삭제
	 @GetMapping("/item/admin/removeitemBuy") 
	 public String removeitemBuy(@RequestParam(name="itemBuyCode", required = false) String
	 itemBuyCode ,Model model) {
	 System.out.println("장비구입 삭제화면에 입력받은 값 ->" + itemBuyCode);
	 itemService.removeitemBuy(itemBuyCode);
	 model.addAttribute("itemBuyCode", itemBuyCode);
	 	  
	 return "redirect:/item/admin/itemBuyList";
	  }
	 
	//장비구입내역 수정
	@PostMapping("/item/admin/modifyitemBuy")
	public String modifyitemBuy(Item item) {
		System.out.println("장비구입 수정화면에서 입력 받은 값->"+ item);
		itemService.modifyitemBuy(item);
		return "redirect:/item/admin/itemBuyList";
	}

	  @GetMapping("/item/admin/modifyitemBuy") 
	  public String modifyitemBuy(@RequestParam(name="itemBuyCode", required = false) String itemBuyCode, Model model) { 
		  System.out.println("장비구입 수정화면에 입력받은 값 ->" +  itemBuyCode);
		  Item item = itemService.itemListByCode(itemBuyCode);
		  model.addAttribute("item", item);
	  
	  return "item/admin/uItemBuy"; 
	  }

	//장비구입내역
	@GetMapping("/item/admin/itemBuyList")
	public String itemBuyList(Model model) {
		List<Item> itemBuyList = itemService.itemBuyList();
		System.out.println("장비구입내역");
		System.out.println(itemBuyList);
		model.addAttribute("itemBuyList", itemBuyList);

		return "item/admin/itemBuyList";
	}

	//장비구입등록
	@PostMapping("/item/admin/addItemBuy") 
	public String addItemBuy(Model model, Item  item, HttpSession session,
			@RequestParam(name = "userBuyId", required = false) String userBuyId) {
		System.out.println(item +	  "=========== 장비 넘어온 값 ============"); 
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(userBuyId );

		System.out.println(sessionId); 
		item.setItemDetailUserId(sessionId);
		
		String result = itemService.addItemBuy(item); 
		System.out.println(result);

		return "redirect:/item/admin/itemBuyList"; 
	}

	@GetMapping("/item/admin/addItemBuy") 
	public String addItemBuy(Model model, HttpSession session) {
		
		String storeId = (String) session.getAttribute("SID");
		
		List<Store> storeList = itemService.addItemChoice(storeId);
		List<Item> itemList = itemService.itemDetailBuy(storeId);
		
		model.addAttribute("storeList", storeList);
		model.addAttribute("itemList", itemList);
		
		System.out.println(storeList);
		
		model.addAttribute("title", "장비수리 등록 하기"); 
		return "item/admin/itemBuyForm2"; 
	}

	//장비수리내역 삭제
	@GetMapping("/item/admin/removeitemRepair") 
	 public String removeitemRepair(@RequestParam(name="itemRepairCode", required = false) String itemRepairCode ,Model model) {
		 System.out.println("장비구입 수정화면에 입력받은 값 ->" + itemRepairCode);
	 itemService.removeitemRepair(itemRepairCode);
	 model.addAttribute("itemRepairCode", itemRepairCode);
	 	  
	 return "redirect:/item/admin/itemRepairList";
	  }

	//장비수리내역 수정
	@PostMapping("/item/admin/modifyitemRepair")
	public String modifyitemRepair(Item item) {
		System.out.println("장비구입 수정화면에서 입력 받은 값->"+ item);
		itemService.modifyitemRepair(item);
		return "redirect:/item/admin/itemRepairList";
	}

	@GetMapping("/item/admin/modifyitemRepair") 
	public String modifyitemRepair(@RequestParam(name="itemRepairCode", required = false) String itemRepairCode, Model model) { 
	  System.out.println("장비구입 수정화면에 입력받은 값 ->" +  itemRepairCode);
	  Item item = itemService.itemRepairByCode(itemRepairCode);
	  model.addAttribute("item", item);
	  
	  return "item/admin/uitemRepair"; 
	  }
		
	//장비수리내역
	@GetMapping("/item/admin/itemRepairList")
	public String itemRepairList(Model model) {
		List<Item> itemRepairList = itemService.itemRepairList();

		model.addAttribute("title", "장비수리내역");
		System.out.println(itemRepairList);
		model.addAttribute("itemRepairList", itemRepairList);

		return "item/admin/itemRepairList";
	}

	//장비수리등록
	@PostMapping("/item/admin/addItemRepair") public String addItemRepair(Model model, Item  item, HttpSession session,
			@RequestParam(name = "userRepairId", required = false) String userId) {
		System.out.println(item +	  "=========== 장비 넘어온 값 ============"); 
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(userId );

		System.out.println(sessionId); item.setItemDetailUserId(sessionId);

		String result = itemService.addItemRepair(item); 
		System.out.println(result);

		return "redirect:/item/admin/itemRepairList"; 
	}

	@GetMapping("/item/admin/addItemRepair") public String addItemRepair(Model model, HttpSession session) {
		
		String storeId = (String) session.getAttribute("SID");
		
		List<Store> storeList = itemService.addItemChoice(storeId);
		List<Item> itemList = itemService.itemDetailBuy(storeId);
		
		model.addAttribute("storeList", storeList);
		model.addAttribute("itemList", itemList);
		
		model.addAttribute("title", "장비수리 등록 하기"); 
		return "item/admin/itemRepairForm"; 
	}

	//장비파기내역 삭제
	@GetMapping("/item/admin/removeitemDelete") 
	 public String removeitemDelete(@RequestParam(name="itemDeleteCode", required = false) String itemDeleteCode ,Model model) {
		 System.out.println("장비파기 삭제화면에 입력받은 값 ->" + itemDeleteCode);
		 itemService.removeitemDelete(itemDeleteCode);
		 model.addAttribute("itemDeleteCode", itemDeleteCode);
		 return "redirect:/item/admin/itemDeleteList";
	  }
	
	//장비파기내역 수정
	@PostMapping("/item/admin/modifyitemDelete")
	public String modifyitemDelete(Item item) {
		System.out.println("장비파기 수정화면에서 입력 받은 값->"+ item);
		itemService.modifyitemDelete(item);
		return "redirect:/item/admin/itemDeleteList";
	}

	@GetMapping("/item/admin/modifyitemDelete") 
	public String modifyitemDelete(@RequestParam(name="itemDeleteCode", required = false) String itemDeleteCode, Model model) { 
	  System.out.println("장비구입 수정화면에 입력받은 값 ->" +  itemDeleteCode);
	  Item item = itemService.itemDeleteByCode(itemDeleteCode);
	  model.addAttribute("item", item);
	  
	  return "item/admin/uItemDelete"; 
	  }
	
	//장비파기내역
	@GetMapping("/item/admin/itemDeleteList")
	public String itemDeleteList(Model model) {
		List<Item> itemDeleteList = itemService.itemDeleteList();
		System.out.println("장비파기내역");
		System.out.println(itemDeleteList);
		model.addAttribute("itemDeleteList", itemDeleteList);

		return "item/admin/itemDeleteList";
	}

	//장비파기등록
	@PostMapping("/item/admin/addItemDelete") public String addItemDelete(Model model, Item  item, HttpSession session,
			@RequestParam(name = "userDeleteId", required = false) String userDeleteId) {
		System.out.println(item +	  "=========== 장비 넘어온 값 ============"); 
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(userDeleteId );

		System.out.println(sessionId); 
		
		item.setItemDetailUserId(sessionId);

		String result = itemService.addItemDelete(item); 
		System.out.println(result);

		return "redirect:/item/admin/itemDeleteList"; 

	}

	@GetMapping("/item/admin/addItemDelete") public String addItemDelte(Model model, HttpSession session) {
		
		String storeId = (String) session.getAttribute("SID");
		
		List<Store> storeList = itemService.addItemChoice(storeId);
		List<Item> itemList = itemService.itemDetailBuy(storeId);
		
		model.addAttribute("storeList", storeList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("title", "장비 등록 하기"); 
		return "item/admin/itemDeleteForm"; 
	}

	//장비수량목록
	@GetMapping("/item/admin/itemCountList")
	public String itemCountList(Model model) {

		List<Item> itemTotalLi = itemService.itemCountList();

		model.addAttribute("title", "장비 수량 목록");
		model.addAttribute("itemTotalLi", itemTotalLi);

		return "item/admin/itemTotalAmount";
	}

	//장비목록
	@GetMapping("/item/admin/itemList")
	public String itemList(Model model) {

		List<Item> itemLi = itemService.itemList();

		model.addAttribute("title", "장비 목록");
		model.addAttribute("itemLi", itemLi);

		return "item/admin/itemDetail";
	}

	//장비등록
	@PostMapping("/item/seller/addItem")
	public String addMyItem(Model model, Item item, HttpSession session, Store store
			,@RequestParam(name = "code", required = false) int code) {
		System.out.println(item + "=========== 장비 넘어온 값 ============");
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(sessionId);
		item.setItemDetailUserId(sessionId);
		System.out.println(code + "포스트 맵핑 장비등록 스토어코드");
		item.setStoreDetailCode(code);
		String result = itemService.addItem(item);
		
		System.out.println(result);
		
		return "redirect:/admin";
	}
	
	//장비등록
	@PostMapping("/item/admin/addItem")
	public String addItem(Model model, Item item, HttpSession session, Store store
							,@RequestParam(name = "code", required = false) int code) {
		System.out.println(item + "=========== 장비 넘어온 값 ============");
		String sessionId = (String) session.getAttribute("SID");
		System.out.println(sessionId);
		item.setItemDetailUserId(sessionId);
		System.out.println(code + "포스트 맵핑 장비등록 스토어코드");
		item.setStoreDetailCode(code);
		String result = itemService.addItem(item);
		
		System.out.println(result);

		return "redirect:/item/admin/itemList";
	}

	@GetMapping("/item/seller/addItem")
	public String addMyItem(Model model, Item item
			,@RequestParam(name = "storeCode", required = false) int storeCode) {
		
		System.out.println(storeCode);
		item.setStoreDetailCode(storeCode);
		int code = item.getStoreDetailCode();
		System.out.println(code);
		model.addAttribute("code", code);
		
		model.addAttribute("title", "장비 등록 하기");
		
		return "item/seller/itemBuyForm";
	}
	
	@GetMapping("/item/admin/addItem")
	public String addItem(Model model, Item item
			,@RequestParam(name = "storeCode", required = false) int storeCode) {

		System.out.println(storeCode);
		item.setStoreDetailCode(storeCode);
		int code = item.getStoreDetailCode();
		System.out.println(code);
		model.addAttribute("code", code);

		model.addAttribute("title", "장비 등록 하기");

		return "item/admin/itemBuyForm";
	}

	@GetMapping("/item/admin/itemInfo")
	public String itemInfo(Model model, Item item
			,@RequestParam(name = "storeItemCode", required = false) int storeItemCode) {

		model.addAttribute("title", "장비 상세 정보");
		model.addAttribute("item", item);

		return "item/admin/itemInfo";
	}

	@GetMapping("/item/seller/addItemChoice")
	public String addMyItemChoice(Model model, HttpSession session) {
		
		String storeId = (String) session.getAttribute("SID");
		
		List<Store> itemList = itemService.addItemChoice(storeId);
		
		model.addAttribute("title", "장비 등록");
		model.addAttribute("itemList", itemList);
		
		return "item/seller/itemBuyChoice";
	}
	
	@GetMapping("/item/admin/addItemChoice")
	public String addItemChoice(Model model, HttpSession session) {

		String storeId = (String) session.getAttribute("SID");

		List<Store> itemList = itemService.addItemChoice(storeId);

		model.addAttribute("title", "장비 등록");
		model.addAttribute("itemList", itemList);

		return "item/admin/itemBuyChoice";
	}

}
