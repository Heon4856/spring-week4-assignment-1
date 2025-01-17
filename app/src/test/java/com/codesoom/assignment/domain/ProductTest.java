package com.codesoom.assignment.domain;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CatProduct model에 대한 테스트")
class ProductTest {

    private static final Long ID = 1L;
    private static final String NAME = "TEST";
    private static final String MAKER = "GUCCI";
    private static final int PRICE = 100000;
    private static final String IMAGE = "image";


    private Product product;


    @BeforeEach
    void setUp() {
        product = Product.builder()
            .id(ID)
            .name(NAME)
            .maker(MAKER)
            .price(PRICE)
            .imgUrl(IMAGE)
            .build();
    }

    @Test
    @DisplayName("Product의 속성설정 테스트")
    public void settings(){
        assertThat(product.getId()).isEqualTo(ID);
        assertThat(product.getName()).isEqualTo(NAME);
        assertThat(product.getMaker()).isEqualTo(MAKER);
        assertThat(product.getPrice()).isEqualTo(PRICE);
        assertThat(product.getImgUrl()).isEqualTo(IMAGE);

    }
}
