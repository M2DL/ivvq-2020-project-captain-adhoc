package captainadhoc.captainadhocbackend.integrationServices;

import captainadhoc.captainadhocbackend.domain.Produit;
import captainadhoc.captainadhocbackend.dto.AchatDto;
import captainadhoc.captainadhocbackend.dto.ProduitsAchatDto;
import captainadhoc.captainadhocbackend.domain.Commande;
import captainadhoc.captainadhocbackend.domain.CommandeProduit;
import captainadhoc.captainadhocbackend.exceptions.InsufficientQuantityException;
import captainadhoc.captainadhocbackend.services.interfaces.ICommandeProduitService;
import captainadhoc.captainadhocbackend.services.interfaces.ICommandeService;
import captainadhoc.captainadhocbackend.services.interfaces.IProduitService;
import captainadhoc.captainadhocbackend.services.interfaces.IUtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class CommandeServiceIntegrationTest {

    @Autowired
    private ICommandeService commandeService;

    private Commande commande;

    private DataLoader dataLoader;

    @Autowired
    private IProduitService produitService;

    @Autowired
    private IUtilisateurService utilisateurService;

    @Autowired
    private ICommandeProduitService commandeProduitService;

    @BeforeEach
    public void setup() {

        commande = Commande.builder()
                .date_commande(new Date())
                .code("code")
                .build();

        dataLoader = new DataLoader(produitService, utilisateurService, commandeService, commandeProduitService);
        dataLoader.run();
    }

    @Test
    void findAllCommandesTest() {
        // given: un DataLoader initialisant la base des commandes

        // when: la liste des commandes est récupérée
        ArrayList<Commande> commandes = commandeService.findAllCommandes();

        // then: on a récupérer l'ensemble des commandes
        assertEquals(3, commandes.size());
    }

    @Test
    void saveCommandeTest() {

        // la commande n'a pas d'ID
        assertNull(commande.getId_commande());

        // when: util est persistée
        commandeService.saveCommande(commande);

        // then: util a id
        assertNotNull(commande.getId_commande());

        // then: on a récupérer l'ensemble des produits
        assertEquals(4, commandeService.findAllCommandes().size());
    }

    @Test
    public void newCommandeTest() {

        List<Produit> produitList = produitService.findAllProduits();
        Long idProduitAchete1 = produitList.get(0).getId_produit();
        Long idProduitAchete2 = produitList.get(1).getId_produit();

        ProduitsAchatDto produitsAchat1 = new ProduitsAchatDto(idProduitAchete1, 2);
        ProduitsAchatDto produitsAchat2 = new ProduitsAchatDto(idProduitAchete2, 3);

        List<ProduitsAchatDto> produitsAchats = new ArrayList<>();
        produitsAchats.add(produitsAchat1);
        produitsAchats.add(produitsAchat2);

        AchatDto achat = new AchatDto("CODETEST", produitsAchats);

        commandeService.newCommande(
                achat,
                utilisateurService.findByNomUtilisateur("Kevin"));

        ArrayList<Commande> commandes = commandeService.findAllCommandes();

        //then : une nouvelle commande a été ajoutée en base
        assertEquals(4, commandes.size());

        Commande commande = commandes.get(3);
        //then : le bon code est associé à la commande
        assertEquals("CODETEST", commande.getCode());

        //then : le bon nombre de ProduitCommande est associé à la commande
        assertEquals(2, commande.getCommandeProduitsList().size());

        CommandeProduit commandeProduit1 = commande.getCommandeProduitsList().get(0);
        CommandeProduit commandeProduit2 = commande.getCommandeProduitsList().get(1);

        //then : la bon produit avec la bonne quantite sont associés
        assertEquals(idProduitAchete1, commandeProduit1.getProduit().getId_produit());
        assertEquals(2, commandeProduit1.getQuantite_commande_produit());

        assertEquals(idProduitAchete2, commandeProduit2.getProduit().getId_produit());
        assertEquals(3, commandeProduit2.getQuantite_commande_produit());

    }

    @Test
    public void newCommandeTestException() {

        List<Produit> produitList = produitService.findAllProduits();
        Long idProduitAchete1 = produitList.get(0).getId_produit();
        Long idProduitAchete2 = produitList.get(1).getId_produit();

        ProduitsAchatDto produitsAchat1 = new ProduitsAchatDto(idProduitAchete1, 200);
        ProduitsAchatDto produitsAchat2 = new ProduitsAchatDto(idProduitAchete2, 300);

        List<ProduitsAchatDto> produitsAchats = new ArrayList<>();
        produitsAchats.add(produitsAchat1);
        produitsAchats.add(produitsAchat2);

        AchatDto achat = new AchatDto("CODETEST", produitsAchats);


        // when: la méthode newCommande est invoquée
        // then: une exception est levé
        assertThrows(InsufficientQuantityException.class, () ->
                commandeService.newCommande(
                        achat,
                        utilisateurService.findByNomUtilisateur("Kevin"))
        );
    }
}
