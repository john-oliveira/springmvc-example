package br.com.fluentcode.springmvc.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.fluentcode.springmvc.dao.ProductDAO;
import br.com.fluentcode.springmvc.entity.Product;

@Controller
@RequestMapping(value = "/products")
public class ProductsController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductDAO productDAO;

	// url: /products/list.html
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		logger.info("Listando os produtos");
		// view
		ModelAndView modelAndView = new ModelAndView("product/list");
		modelAndView.addObject("products", productDAO.findAll());
		return modelAndView;
	}

	// url: /products/detail/1.html
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public ModelAndView detail(@PathVariable("id") Integer id) {
		logger.info("Redirecionando para a tela de detalhes do produto");
		// view
		ModelAndView modelAndView = new ModelAndView("product/detail");
		modelAndView.addObject("product", productDAO.findById(id));
		return modelAndView;
	}
	
	// url: /products/create.html
	@RequestMapping(value = "/create", method=RequestMethod.GET)
	public ModelAndView create() {
		logger.info("Redirecionando para a tela de cria��o do produto");
		// view
		ModelAndView modelAndView = new ModelAndView("product/create");
		modelAndView.addObject("product", new Product());
		return modelAndView;
	}
	
	// url: /products/save.html
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@Valid Product product, BindingResult result, Model model) {
		logger.info("Salvando o produto: {}", product.getName());
		if(result.hasErrors()){
			logger.warn("The Product contains invalid data");
			model.addAttribute("product", product);
			return "/product/create";//TODO Ver como direcionar para a p�gina anterior
		}
		productDAO.save(product);
		// invokes the list method
		return "redirect:/products/list.html";
	}
	
	// url: /products/edit/1.html
	@RequestMapping(value = "/edit/{id}", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Integer id) {
		logger.info("Redirecionando para a tela de edi��o do produto: {}", id);
		// view
		ModelAndView modelAndView = new ModelAndView("product/edit");
		modelAndView.addObject("product", productDAO.findById(id));
		return modelAndView;
	}
	
	// url: /products/update.html
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@Valid Product product, BindingResult result, Model model) {
		logger.info("Atualizando o produto: id {}, name {}", product.getId(), product.getName());
		if(result.hasErrors()){
			logger.warn("The Product contains invalid data");
			model.addAttribute("product", product);
			return "/product/edit";//TODO Ver como direcionar para a p�gina anterior
		}
		productDAO.save(product);
		// invokes the list method
		return "redirect:/products/list.html";
	}
	
	// url: /products/delete/1.html
	@RequestMapping(value = "/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable("id") Integer id) {
		logger.info("Deletando o produto: {}", id);
		productDAO.delete(id);
		// invokes the list method
		return "redirect:/products/list.html";
	}


}
