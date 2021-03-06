package com.camilasoares.cursomc.services;

import com.camilasoares.cursomc.domain.Category;
import com.camilasoares.cursomc.domain.Product;
import com.camilasoares.cursomc.repositories.CategoryRepository;
import com.camilasoares.cursomc.repositories.ProductRepository;
import com.camilasoares.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;


	public Product find(Integer id) throws ObjectNotFoundException {
		Optional<Product> product = productRepository.findById (id);
		return product.orElseThrow ( ()-> new ObjectNotFoundException (
				"Objeto não encontrado! Id:" + id + ", Tipo:" + Product.class.getName ()
		) );
	}
	
	public Page<Product> seach(String nome, List<Integer> ids, Integer page,Integer linesForpage, String orderBy, String direction) {
		PageRequest pageRequest = new PageRequest(page, linesForpage, Direction.valueOf(direction), orderBy);
		List<Category> categorias = categoryRepository.findAllById (ids);
		return productRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

	

}
