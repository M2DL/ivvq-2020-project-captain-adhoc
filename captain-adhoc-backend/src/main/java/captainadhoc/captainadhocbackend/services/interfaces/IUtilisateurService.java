package captainadhoc.captainadhocbackend.services.interfaces;

import captainadhoc.captainadhocbackend.domain.Utilisateur;

public interface IUtilisateurService {

    Utilisateur saveUtilisateur(Utilisateur utilisateur);

    Utilisateur findById(Long id);

    Utilisateur findByNomUtilisateur(String nomUtilisateur);
}
