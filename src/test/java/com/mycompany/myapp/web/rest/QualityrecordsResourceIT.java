package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Qualityrecords;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.QualityrecordsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link QualityrecordsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QualityrecordsResourceIT {

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEST_2 = 1;
    private static final Integer UPDATED_TEST_2 = 2;

    private static final String ENTITY_API_URL = "/api/qualityrecords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QualityrecordsRepository qualityrecordsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Qualityrecords qualityrecords;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Qualityrecords createEntity(EntityManager em) {
        Qualityrecords qualityrecords = new Qualityrecords().supplier(DEFAULT_SUPPLIER).test2(DEFAULT_TEST_2);
        return qualityrecords;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Qualityrecords createUpdatedEntity(EntityManager em) {
        Qualityrecords qualityrecords = new Qualityrecords().supplier(UPDATED_SUPPLIER).test2(UPDATED_TEST_2);
        return qualityrecords;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Qualityrecords.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        qualityrecords = createEntity(em);
    }

    @Test
    void createQualityrecords() throws Exception {
        int databaseSizeBeforeCreate = qualityrecordsRepository.findAll().collectList().block().size();
        // Create the Qualityrecords
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeCreate + 1);
        Qualityrecords testQualityrecords = qualityrecordsList.get(qualityrecordsList.size() - 1);
        assertThat(testQualityrecords.getSupplier()).isEqualTo(DEFAULT_SUPPLIER);
        assertThat(testQualityrecords.getTest2()).isEqualTo(DEFAULT_TEST_2);
    }

    @Test
    void createQualityrecordsWithExistingId() throws Exception {
        // Create the Qualityrecords with an existing ID
        qualityrecords.setId(1L);

        int databaseSizeBeforeCreate = qualityrecordsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSupplierIsRequired() throws Exception {
        int databaseSizeBeforeTest = qualityrecordsRepository.findAll().collectList().block().size();
        // set the field null
        qualityrecords.setSupplier(null);

        // Create the Qualityrecords, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllQualityrecords() {
        // Initialize the database
        qualityrecordsRepository.save(qualityrecords).block();

        // Get all the qualityrecordsList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(qualityrecords.getId().intValue()))
            .jsonPath("$.[*].supplier")
            .value(hasItem(DEFAULT_SUPPLIER))
            .jsonPath("$.[*].test2")
            .value(hasItem(DEFAULT_TEST_2));
    }

    @Test
    void getQualityrecords() {
        // Initialize the database
        qualityrecordsRepository.save(qualityrecords).block();

        // Get the qualityrecords
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, qualityrecords.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(qualityrecords.getId().intValue()))
            .jsonPath("$.supplier")
            .value(is(DEFAULT_SUPPLIER))
            .jsonPath("$.test2")
            .value(is(DEFAULT_TEST_2));
    }

    @Test
    void getNonExistingQualityrecords() {
        // Get the qualityrecords
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQualityrecords() throws Exception {
        // Initialize the database
        qualityrecordsRepository.save(qualityrecords).block();

        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();

        // Update the qualityrecords
        Qualityrecords updatedQualityrecords = qualityrecordsRepository.findById(qualityrecords.getId()).block();
        updatedQualityrecords.supplier(UPDATED_SUPPLIER).test2(UPDATED_TEST_2);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedQualityrecords.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedQualityrecords))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
        Qualityrecords testQualityrecords = qualityrecordsList.get(qualityrecordsList.size() - 1);
        assertThat(testQualityrecords.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
        assertThat(testQualityrecords.getTest2()).isEqualTo(UPDATED_TEST_2);
    }

    @Test
    void putNonExistingQualityrecords() throws Exception {
        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();
        qualityrecords.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, qualityrecords.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQualityrecords() throws Exception {
        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();
        qualityrecords.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQualityrecords() throws Exception {
        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();
        qualityrecords.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQualityrecordsWithPatch() throws Exception {
        // Initialize the database
        qualityrecordsRepository.save(qualityrecords).block();

        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();

        // Update the qualityrecords using partial update
        Qualityrecords partialUpdatedQualityrecords = new Qualityrecords();
        partialUpdatedQualityrecords.setId(qualityrecords.getId());

        partialUpdatedQualityrecords.supplier(UPDATED_SUPPLIER).test2(UPDATED_TEST_2);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQualityrecords.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQualityrecords))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
        Qualityrecords testQualityrecords = qualityrecordsList.get(qualityrecordsList.size() - 1);
        assertThat(testQualityrecords.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
        assertThat(testQualityrecords.getTest2()).isEqualTo(UPDATED_TEST_2);
    }

    @Test
    void fullUpdateQualityrecordsWithPatch() throws Exception {
        // Initialize the database
        qualityrecordsRepository.save(qualityrecords).block();

        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();

        // Update the qualityrecords using partial update
        Qualityrecords partialUpdatedQualityrecords = new Qualityrecords();
        partialUpdatedQualityrecords.setId(qualityrecords.getId());

        partialUpdatedQualityrecords.supplier(UPDATED_SUPPLIER).test2(UPDATED_TEST_2);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQualityrecords.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQualityrecords))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
        Qualityrecords testQualityrecords = qualityrecordsList.get(qualityrecordsList.size() - 1);
        assertThat(testQualityrecords.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
        assertThat(testQualityrecords.getTest2()).isEqualTo(UPDATED_TEST_2);
    }

    @Test
    void patchNonExistingQualityrecords() throws Exception {
        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();
        qualityrecords.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, qualityrecords.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQualityrecords() throws Exception {
        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();
        qualityrecords.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQualityrecords() throws Exception {
        int databaseSizeBeforeUpdate = qualityrecordsRepository.findAll().collectList().block().size();
        qualityrecords.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(qualityrecords))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Qualityrecords in the database
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQualityrecords() {
        // Initialize the database
        qualityrecordsRepository.save(qualityrecords).block();

        int databaseSizeBeforeDelete = qualityrecordsRepository.findAll().collectList().block().size();

        // Delete the qualityrecords
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, qualityrecords.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Qualityrecords> qualityrecordsList = qualityrecordsRepository.findAll().collectList().block();
        assertThat(qualityrecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
