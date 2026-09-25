package com.nexion.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.nexion.backend.dto.AddMemberRequest;
import com.nexion.backend.dto.MemberResponse;
import com.nexion.backend.dto.UpdateMemberRoleRequest;
import com.nexion.backend.dto.WalletRequest;
import com.nexion.backend.dto.WalletResponse;
import com.nexion.backend.entity.User;
import com.nexion.backend.entity.Wallet;
import com.nexion.backend.entity.WalletMember;
import com.nexion.backend.enums.WalletRole;
import com.nexion.backend.exception.BusinessException;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.UserRepository;
import com.nexion.backend.repository.WalletMemberRepository;
import com.nexion.backend.repository.WalletRepository;
import jakarta.transaction.Transactional;

@Service
public class WalletService {

    private final WalletRepository repository;
    private final WalletMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final UserLogService userLogService;

    public WalletService(WalletRepository repository, WalletMemberRepository memberRepository,
            UserRepository userRepository, UserLogService userLogService) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.userLogService = userLogService;
    }

    @Transactional
    public WalletResponse criar(WalletRequest request) {
        User owner = userLogService.get();

        // para criar a carteira oficialmente
        Wallet wallet = new Wallet();
        wallet.setOwner(owner);
        wallet.setName(request.getName());
        wallet.setDescripton(request.getDescription());
        Wallet salva = repository.save(wallet);

        // adiciona como owner automáticamente
        WalletMember membro = new WalletMember();
        membro.setWallet(salva);
        membro.setUser(owner);
        membro.setRole(WalletRole.OWNER);
        memberRepository.save(membro);

        return toResponse(salva);
    }

    public List<WalletResponse> listarTodos() {
        Long userId = userLogService.get().getId();
        return memberRepository.findByUserId(userId)
                .stream().map(WalletMember::getWallet).map(this::toResponse).toList();

    }

    public WalletResponse buscarPorId(Long id) {
        verificarMembro(id);
        return toResponse(buscarEntidade(id));
    }

    public void remover(Long id) {
        Wallet wallet = buscarEntidade(id);
        verificarDono(wallet);
        repository.deleteById(id);
    }

    // Dos membros
    public List<MemberResponse> listarMembros(Long walletId) {
        verificarMembro(walletId);
        return memberRepository.findByWalletId(walletId).stream().map(this::toMemberResponse).toList();
    }

    public MemberResponse adicionarMembro(Long walletId, AddMemberRequest request) {
        Wallet wallet = buscarEntidade(walletId);
        verificarDono(wallet);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (memberRepository.existsByWalletIdAndUserId(walletId, user.getId())) {
            throw new BusinessException("Usuário ja é membro");
        }
        WalletMember membro = new WalletMember();
        membro.setWallet(wallet);
        membro.setUser(user);
        membro.setRole(request.getRole());
        return toMemberResponse(memberRepository.save(membro));
    }

    public MemberResponse alterarPapel(Long walletId, Long userId, UpdateMemberRoleRequest request) {
        verificarDono(buscarEntidade(walletId));

        WalletMember membro = memberRepository.findByWalletIdAndUserId(walletId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado"));
        membro.setRole(request.getRole());
        return toMemberResponse(memberRepository.save(membro));
    }

    public void removerMembro(Long walletId, Long userId) {
        verificarDono(buscarEntidade(walletId));
        WalletMember membro = memberRepository.findByWalletIdAndUserId(walletId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum membro encontrado"));
        memberRepository.delete(membro);
    }

    private Wallet buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));
    }

    private WalletResponse toResponse(Wallet wallet) {
        WalletResponse response = new WalletResponse();
        response.setId(wallet.getId());
        response.setName(wallet.getName());
        response.setDescription(wallet.getDescripton());
        response.setOwnerId(wallet.getOwner().getId());
        response.setCreatedAt(wallet.getCreatedAt());
        return response;
    }

    private MemberResponse toMemberResponse(WalletMember member) {
        MemberResponse response = new MemberResponse();
        response.setUserId(member.getUser().getId());
        response.setNome(member.getUser().getName());
        response.setEmail(member.getUser().getEmail());
        response.setRole(member.getRole());
        return response;
    }

    private void verificarMembro(Long walletId) {
        Long userId = userLogService.get().getId();

        if (!memberRepository.existsByWalletIdAndUserId(walletId, userId)) {
            throw new ResourceNotFoundException("A carteira não foi encontrada");
        }
    }

    private void verificarDono(Wallet wallet) {
        Long userId = userLogService.get().getId();
        if (!wallet.getOwner().getId().equals(userId)) {
            throw new ResourceNotFoundException("A carteira não foi encontrada");

        }
    }
}
