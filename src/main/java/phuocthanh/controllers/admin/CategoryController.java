package phuocthanh.controllers.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import phuocthanh.entity.Category;
import phuocthanh.models.CategoryModel;
import phuocthanh.services.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@RequestMapping("")
	public String all(Model model) {
		
		List<Category> list=categoryService.findAll();
		model.addAttribute("list",list);
		return "admin/category/list";
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		CategoryModel category=new CategoryModel();
//		category.setIsEdit(true);
		model.addAttribute("category",category);
		return "admin/category/add";
	}
	
	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Valid @ModelAttribute("category") CategoryModel  cateModel,BindingResult result) {
		if(result.hasErrors()) {
			return new ModelAndView("admin/category/add");
		}
		Category entity =  new Category();
		//copy tu Model sang Entity
		BeanUtils.copyProperties(cateModel,entity);
		//
		categoryService.save(entity);
		//
		String message="";
		if(cateModel.getIsEdit()) {
			message="Category is Edited!!!!";
		}else {
			message="Category is Saved!!!!";
		}
		model.addAttribute("message",message);
		//
		return new ModelAndView("forward:/admin/categories",model);
	}
	
	@GetMapping("/edit{id}")
	public ModelAndView edit(ModelMap model,@PathVariable("id") Long categoryId) {
		Optional<Category> optCategory=categoryService.findById(categoryId);
		CategoryModel  cateModel=new CategoryModel();
		//Kiem tra  su ton tai cua category
		if(optCategory.isPresent()) {
			Category entity=optCategory.get();
			//copy tu Model sang Entity
			BeanUtils.copyProperties(entity,cateModel);
			cateModel.setIsEdit(true);
			//day du lieu ra view
			model.addAttribute("category",cateModel);
			
			return new ModelAndView("admin/category/add",model);
		}
		model.addAttribute("message","Category is not existed!!!");
		return new ModelAndView("forward:/admin/categories",model);
	}
}
