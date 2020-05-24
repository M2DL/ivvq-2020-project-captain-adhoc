package captainadhoc.captainadhocbackend.services;

import captainadhoc.captainadhocbackend.domain.Produit;
import captainadhoc.captainadhocbackend.exceptions.InsufficientQuantityException;
import captainadhoc.captainadhocbackend.services.implementations.ProduitService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import captainadhoc.captainadhocbackend.repositories.ProduitRepository;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitService produitService;

    @Test
    public void testSaveProduit() {
        //given un produit
        Produit produit = Produit.builder()
                .quantite_produit(15)
                .nom_produit("produit")
                .description_produit("description")
                .image_produit("image")
                .prix_produit(1)
                .build();

        when(produitService.getProduitRepository().save(produit)).thenReturn(produit);

        // when: la méthode saveProduit est invoquée
        produitService.saveProduit(produit);

        // then: la méthode save du ProduitRepository associé est invoquée
        verify(produitService.getProduitRepository()).save(produit);
    }

    @Test
    public void testFindAllProduits() {
        // given: un ProduitService
        // when: la méthode findAllProduits est invoquée
        produitService.findAllProduits();

        // then: la méthode findAll du Repository associé est invoquée
        verify(produitService.getProduitRepository()).findAll();
    }

    @Test
    public void testModifierQuantite() {
        //given un produit
        Produit produit = Produit.builder()
                .id_produit(1L)
                .quantite_produit(15)
                .nom_produit("produit")
                .description_produit("description")
                .image_produit("image")
                .prix_produit(1)
                .build();

        when(produitService.getProduitRepository().save(produit)).thenReturn(produit);
        when(produitService.getProduitRepository().findById(1L)).thenReturn(Optional.of(produit));

        // when: la méthode modifierQuantite est invoquée
        produitService.modifierQuantite(produit.getId_produit(),20);

        // then: la méthode save du ProduitRepository associé est invoquée
        verify(produitService.getProduitRepository()).save(produit);

        // then: la méthode findById du ProduitRepository associé est invoquée
        verify(produitService.getProduitRepository()).findById(produit.getId_produit());

        // then: la quantite du produit a été mis à jour
        assertEquals(20, produit.getQuantite_produit());
    }

    @Test
    public void testDecrementQuantity() {
        //given un produit
        Produit produit = Produit.builder()
                .id_produit(1L)
                .quantite_produit(15)
                .nom_produit("produit")
                .description_produit("description")
                .image_produit("image")
                .prix_produit(1)
                .build();

        //given: la quantité du produit acheté
        int quantiteProduit = 5;

        when(produitService.getProduitRepository().findById(produit.getId_produit())).thenReturn(Optional.of(produit));
        when(produitService.getProduitRepository().save(produit)).thenReturn(produit);

        //when: la méthode decrementQuantity est invoquée
        produitService.decrementQuantity(produit.getId_produit(), quantiteProduit);

        // then: la méthode findById du ProduitRepository associé est invoquée
        verify(produitService.getProduitRepository()).findById(produit.getId_produit());

        // then: la méthode save du ProduitRepository associé est invoquée
        verify(produitService.getProduitRepository()).save(produit);

        // then: la quantite du produit a été mis à jour
        assertEquals(10, produit.getQuantite_produit());
    }

    @Test
    public void testExceptionDecrementQuantity() {

        //given un produit
        Produit produit = Produit.builder()
                .id_produit(1L)
                .quantite_produit(1)
                .nom_produit("produit")
                .description_produit("description")
                .image_produit("image")
                .prix_produit(1)
                .build();

        //given: la quantité du produit acheté
        int quantiteProduit = 5;

        when(produitService.getProduitRepository().findById(produit.getId_produit())).thenReturn(Optional.of(produit));

        assertThrows(InsufficientQuantityException.class, () ->
                produitService.decrementQuantity(produit.getId_produit(), quantiteProduit)
        );
    }
}
