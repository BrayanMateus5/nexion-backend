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
import com.nexion.backend.repository.UserRepository;
import com.nexion.backend.repository.WalletMemberRepository;
import com.nexion.backend.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletService {

    private final WalletRepository repository;
    private final WalletMemberRepository memberRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository repository, WalletMemberRepository memberRepository,
            UserRepository userRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WalletResponse criar(WalletRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public WalletResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }

    // Dos membros
    public List<MemberResponse> listarMembros(Long walletId) {
        return memberRepository.findByWalletId(walletId).stream().map(this::toMemberResponse).toList();
    }

    public MemberResponse adicionarMembro(Long walletId, AddMemberRequest request) {
        Wallet wallet = buscarEntidade(walletId);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (memberRepository.existsByWalletIdAndUserId(walletId, user.getId())) {
            throw new RuntimeException("Usuário ja é membro");
        }
        WalletMember membro = new WalletMember();
        membro.setWallet(wallet);
        membro.setUser(user);
        membro.setRole(request.getRole());
        return toMemberResponse(memberRepository.save(membro));
    }

    public MemberResponse alterarPapel(Long walletId, Long userId, UpdateMemberRoleRequest request) {
        WalletMember membro = memberRepository.findByWalletIdAndUserId(walletId, userId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado"));
        membro.setRole(request.getRole());
        return toMemberResponse(memberRepository.save(membro));
    }

    public void removerMembro(Long walletId, Long userId) {
        WalletMember membro = memberRepository.findByWalletIdAndUserId(walletId, userId)
                .orElseThrow(() -> new RuntimeException("Nenhum membro encontrado"));
        memberRepository.delete(membro);
    }

    private Wallet buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
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
}
