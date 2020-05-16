package captainadhoc.captainadhocbackend;

import captainadhoc.captainadhocbackend.domain.Commande;
import captainadhoc.captainadhocbackend.domain.CommandeProduit;
import captainadhoc.captainadhocbackend.domain.Produit;
import captainadhoc.captainadhocbackend.domain.Utilisateur;
import captainadhoc.captainadhocbackend.services.interfaces.ICommandeProduitService;
import captainadhoc.captainadhocbackend.services.interfaces.ICommandeService;
import captainadhoc.captainadhocbackend.services.interfaces.IProduitService;
import captainadhoc.captainadhocbackend.services.interfaces.IUtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Component
@Transactional
@Profile("dev")
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    @Autowired
    private IProduitService produitService;

    @Autowired
    private IUtilisateurService utilisateurService;

    @Autowired
    private ICommandeService commandeService;

    @Autowired
    private ICommandeProduitService commandeProduitService;

    public void initProduit(){

        produitService.deleteAllProduit();
        Utilisateur admin = new Utilisateur((long) 1, "Kevin", "Marchand", "marchand1", "mdp", true, emptyList());

        List<Produit> produitList = new ArrayList<>();
        Produit produit2 = new Produit(16, "CyberboX",
                "Non comptant d'avoir les meilleures voitures au MONDE, Tesla propose la meilleure console de jeu grand public !",
                "https://urlz.fr/cHLH", 100000);
        Produit produit1 = new Produit(15, "PS5",
                "Encore une playstation de folie \\o/", "https://urlz.fr/cHLz", 1);
        Produit produit3 = new Produit(2, "Mad box",
                "Cette console va révolutionner le du la de esport !",
                "https://urlz.fr/cHJp", 666);
        Produit produit4 = new Produit(100, "New retro +",
                "Elle fera tourner les jeux dernières générations tels que tetris et même Donkey kong 64 ! Et tout àa pour seulement 1399,99€",
                "https://urlz.fr/cHJz", 10);
        Produit produit5 = new Produit(5, "Xbox Serie X",
                "C'est partiiiii pour la console pc !",
                "https://urlz.fr/cHLM", 200);
        produitList.add(produit1);
        produitList.add(produit2);
        produitList.add(produit3);
        produitList.add(produit4);
        produitList.add(produit5);

        utilisateurService.saveUtilisateur(admin);

        produitService.saveProduit(produit1);
        produitService.saveProduit(produit2);
        produitService.saveProduit(produit3);
        produitService.saveProduit(produit4);
        produitService.saveProduit(produit5);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initProduit();
        initCommandes();
    }

    public void initCommandes () {
        Commande commande1 = new Commande("20/20/2020","code");
        Commande commande2 = new Commande("20/20/2020","code");
        Commande commande3 = new Commande("20/20/2020","");

        CommandeProduit commandeProduit = new CommandeProduit();
        CommandeProduit commandeProduit2 = new CommandeProduit();
        CommandeProduit commandeProduit3 = new CommandeProduit();
        CommandeProduit commandeProduit4 = new CommandeProduit();

        ArrayList<Produit> produitArrayList = produitService.findAllProduits();

        commandeProduit.setProduit(produitArrayList.get(0));
        commandeProduit2.setProduit(produitArrayList.get(0));
        commandeProduit3.setProduit(produitArrayList.get(1));
        commandeProduit4.setProduit(produitArrayList.get(1));

        commandeProduit.setQuantite_commande_produit(1);
        commandeProduit.setQuantite_commande_produit(2);
        commandeProduit.setQuantite_commande_produit(3);
        commandeProduit.setQuantite_commande_produit(4);

        commandeProduit.setCommande(commande1);
        commandeProduit2.setCommande(commande2);
        commandeProduit3.setCommande(commande2);
        commandeProduit4.setCommande(commande3);

        List<CommandeProduit> commandeProduitList = new ArrayList<>();
        commandeProduitList.add(commandeProduit);

        List<CommandeProduit> commandeProduitList2 = new ArrayList<>();
        commandeProduitList2.add(commandeProduit2);
        commandeProduitList2.add(commandeProduit3);

        List<CommandeProduit> commandeProduitList3 = new ArrayList<>();
        commandeProduitList3.add(commandeProduit4);

        commande1.setCommandeProduitsList(commandeProduitList);
        commande2.setCommandeProduitsList(commandeProduitList2);
        commande3.setCommandeProduitsList(commandeProduitList3);

        commandeService.saveCommande(commande1);
        commandeService.saveCommande(commande2);
        commandeService.saveCommande(commande3);

        commandeProduit.setCommande(commande1);
        commandeProduit2.setCommande(commande2);
        commandeProduit3.setCommande(commande2);
        commandeProduit4.setCommande(commande3);

        commandeProduitService.saveCommandeProduit(commandeProduit);
        commandeProduitService.saveCommandeProduit(commandeProduit2);
        commandeProduitService.saveCommandeProduit(commandeProduit3);
        commandeProduitService.saveCommandeProduit(commandeProduit4);
    }
}
