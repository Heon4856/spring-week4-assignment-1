package com.codesoom.assignment.product.application;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.infra.ProductRepository;
import com.codesoom.assignment.product.ui.dto.ProductResponseDto;
import com.codesoom.assignment.product.ui.dto.ProductSaveRequestDto;
import com.codesoom.assignment.product.ui.dto.ProductUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("ProductService 클래스")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private static final Long PRODUCT1_ID = 1L;
    private static final Long PRODUCT2_ID = 2L;
    private static final Long NOT_EXIST_ID = -1L;
    private static final String PRODUCT1_NAME = "product1";
    private static final String PRODUCT2_NAME = "product2";
    private static final String PRODUCT1_MAKER = "maker1";
    private static final String PRODUCT2_MAKER = "maker2";
    private static final String PRODUCT1_IMAGE = "https://http.cat/599";
    private static final String PRODUCT2_IMAGE = "https://http.cat/200";
    private static final int PRODUCT1_PRICE = 10_000;
    private static final int PRODUCT2_PRICE = 20_000;

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private List<Product> products;

    private Product product1;
    private Product product2;

    private ProductResponseDto responseDto1;
    private ProductResponseDto responseDto2;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
        setUpFixtures();
    }

    @Test
    @DisplayName("getProducts 메서드는 등록된 상품 목록을 리턴한다")
    void getProducts() {
        given(productRepository.findAll()).willReturn(products);

        List<ProductResponseDto> products = productService.getProducts();

        assertThat(products).containsExactly(responseDto1, responseDto2);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("getProduct 메서드는 등록된 상품 id에 해당하는 상품을 리턴한다")
    void getProductWithValidId() {
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(product1));

        ProductResponseDto actual = productService.getProduct(anyLong());

        assertAll(
                () -> assertThat(actual).isEqualTo(responseDto1),
                () -> assertThat(actual.getId()).isEqualTo(PRODUCT1_ID)
        );
        verify(productRepository).findById(anyLong());
    }

    @Test
    @DisplayName("getProduct 메서드는 등록되지 않은 상품 id로 상품 조회시 예외가 발생한다.")
    void getProductWithInValidId() {
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.getProduct(NOT_EXIST_ID));
        verify(productRepository).findById(NOT_EXIST_ID);
    }

    @Test
    @DisplayName("updateProduct 메서드는 등록된 id로 상품 갱신할 수 있다")
    void updateProductWithValidId() {
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(product1));
        ProductUpdateRequestDto expected = ProductUpdateRequestDto.builder()
                .name("NEW NAME")
                .maker("NEW MAKER")
                .price(1000)
                .imageUrl("NEW IMAGE")
                .build();

        ProductResponseDto actual = productService.updateProduct(anyLong(), expected);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getMaker()).isEqualTo(expected.getMaker()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }

    @Test
    @DisplayName("updateProduct 메서드는 등록되지 않은 상품 id로 상품 갱신시 예외가 발생한다.")
    void updateProductWithInValidId() {
        ProductUpdateRequestDto requestDto = ProductUpdateRequestDto.builder().build();

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.updateProduct(NOT_EXIST_ID, requestDto));
    }

    @Test
    @DisplayName("deleteProduct 메서드는 등록된 상품 id에 해당하는 상품을 삭제한다")
    void deleteProductWithValidId() {
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(product1));

        productService.deleteProduct(anyLong());

        verify(productRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteProduct 메서드는 등록되지 상품 id로 상품을 삭제시 예외가 발생한다")
    void deleteProductWithInValidId() {
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.deleteProduct(NOT_EXIST_ID));

        verify(productRepository, never()).deleteById(NOT_EXIST_ID);
    }

    @Test
    @DisplayName("createProduct 메서드는 상품을 새로 등록할 수 있다.")
    void createProduct() {
        given(productRepository.save(any(Product.class)))
                .willReturn(product1);

        ProductSaveRequestDto expected = ProductSaveRequestDto.builder()
                .name(PRODUCT1_NAME)
                .maker(PRODUCT1_MAKER)
                .price(PRODUCT1_PRICE)
                .imageUrl(PRODUCT1_IMAGE)
                .build();

        ProductResponseDto actual = productService.createProduct(expected);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getMaker()).isEqualTo(expected.getMaker()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
        verify(productRepository).save(any(Product.class));
    }

    void setUpFixtures() {
        product1 = Product.builder()
                .id(PRODUCT1_ID)
                .name(PRODUCT1_NAME)
                .maker(PRODUCT1_MAKER)
                .price(PRODUCT1_PRICE)
                .imageUrl(PRODUCT1_IMAGE)
                .build();

        product2 = Product.builder()
                .id(PRODUCT2_ID)
                .name(PRODUCT2_NAME)
                .maker(PRODUCT2_MAKER)
                .price(PRODUCT2_PRICE)
                .imageUrl(PRODUCT2_IMAGE)
                .build();

        products = Arrays.asList(product1, product2);
        responseDto1 = ProductResponseDto.of(product1);
        responseDto2 = ProductResponseDto.of(product2);
    }
}