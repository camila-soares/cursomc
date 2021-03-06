package com.camilasoares.cursomc.resources;



import com.camilasoares.cursomc.domain.Category;
import com.camilasoares.cursomc.dto.CategoryDTO;
import com.camilasoares.cursomc.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias")
public class CategoryResource {
	private final CategoryService categoryService;

	@Autowired
	public CategoryResource(CategoryService categoryService){
		this.categoryService = categoryService;
	}

	@ApiOperation ( value = "Busca por id")
	@GetMapping("/{id}")
	public ResponseEntity<Category> find(@PathVariable Integer id)  {
		Category obj = categoryService.find(id);
		return ResponseEntity.ok().body(obj);
		
	}

	@PreAuthorize( "hasAnyRole('ADMIN')" )
	@ApiOperation ( value = "Inserção de Categorias")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoryDTO objDTO){
		Category obj = categoryService.fromDTO(objDTO);
		obj = categoryService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PreAuthorize ( "hasAnyRole('ADMIN')" )
	@ApiOperation ( value = "Atualiza Categorias")
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoryDTO objDTO, @PathVariable Integer id){
		Category obj = categoryService.fromDTO(objDTO);
		obj.setId(id);
		obj = categoryService.update(obj);
		return ResponseEntity.noContent().build();
		
	}

	@PreAuthorize ( "hasAnyRole('ADMIN')" )
	@ApiOperation ( value = "Remove categorias")
	@ApiResponses ( value = {
			@ApiResponse ( code = 400, message= "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse ( code = 404, message = "Código inexistente")})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) throws ObjectNotFoundException {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping
	@ApiOperation ( value = "Retorna todas Categorias")
	public ResponseEntity<List<CategoryDTO>> findAll() throws ObjectNotFoundException {
		List<Category> list = categoryService.findAll();
		List<CategoryDTO> listDTO = list.stream().map(obj -> new CategoryDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
		
	}

	@GetMapping
	@RequestMapping(value="/page")
	public ResponseEntity<Page<CategoryDTO>> findPage(
			@RequestParam(value="page", defaultValue="0")  Integer page,
			@RequestParam(value="linesForpage", defaultValue="24") Integer linesForpage,
			@RequestParam(value="orderBy", defaultValue="nome")  String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC")  String direction) {
		Page<Category> list = categoryService.findPage(page, linesForpage, orderBy, direction);
		Page<CategoryDTO> listDTO = list.map(obj -> new CategoryDTO(obj));
		return ResponseEntity.ok().body(listDTO);
		
	}
	
	

}
