package com.camilasoares.cursomc.services;

import com.camilasoares.cursomc.domain.Cidade;
import com.camilasoares.cursomc.domain.Client;
import com.camilasoares.cursomc.domain.Endereco;
import com.camilasoares.cursomc.domain.enums.ClientType;
import com.camilasoares.cursomc.domain.enums.Perfil;
import com.camilasoares.cursomc.dto.ClientDTO;
import com.camilasoares.cursomc.dto.ClientNewDTO;
import com.camilasoares.cursomc.repositories.AddressRepository;
import com.camilasoares.cursomc.repositories.ClientRepository;
import com.camilasoares.cursomc.security.UserSS;
import com.camilasoares.cursomc.services.exception.AuthorizationException;
import com.camilasoares.cursomc.services.exception.DataIntegrityException;
import com.camilasoares.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private S3Service s3Service;
	
	
	
	public Client find(Integer id) {

		UserSS user = UserService.authenticatedd ();
		if(user==null || !user.hasRole ( Perfil.ADMIN ) && !id.equals ( user.getId () )){
			throw new AuthorizationException ("Acesso Negado");
		}

		Client obj = clientRepository.findOne(id);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Client.class.getName());
		}
		return obj;
	}

	@Transactional
	public  Client insert(Client obj) {
		obj.setId(null);
		obj = clientRepository.save(obj);
		addressRepository.save(obj.getEnderecos());
		return obj;
	}

	public Client update(Client obj) {
		Client newObj  = find(obj.getId());
		updateData(newObj, obj);
		return clientRepository.save(newObj);
	}

	private void updateData(Client newObj, Client obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}

	public void delete(Integer id) throws ObjectNotFoundException {
		find(id);
		try{
		clientRepository.delete(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir  Cliente o mesmo possui pedidos");
		}
		
	}

	public List<Client> findAll() {
		return clientRepository.findAll();
	}
	
	public Page<Client> findPage(Integer page, String linesForpage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, Integer.parseInt ( linesForpage ) , Direction.valueOf(direction), orderBy);
		return clientRepository.findAll(pageRequest);
	}
	
	public Client fromDTO(ClientDTO objDTO) {
		return new Client(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
		
	}
	
	public Client fromDTO(ClientNewDTO objDTO) {
		Client cli = new Client(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), ClientType.toEnum(objDTO.getTipo()), pe.encode ( objDTO.getSenha () ));
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco (null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2() != null){
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		if(objDTO.getTelefone3() != null){
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		return cli;
	}


    public Client findByEmail(String email) {
		UserSS user = UserService.authenticatedd ();
		if(user == null || !user.hasRole (Perfil.ADMIN) && !email.equals(user.getUsername ())){
			throw new AuthorizationException ( "Acesso negado" );
		}
		Client obj = clientRepository.findByEmail ( email );
		if(obj == null){
			throw new ObjectNotFoundException ( "Objeto não encontrado! Id"+ user.getId () + ", Tipo: " + Client.class.getName () );
		}
		return obj;
	}
}
