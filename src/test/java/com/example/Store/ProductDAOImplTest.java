package com.example.Store;

import com.example.Store.dao.ProductDAOImpl;
import com.example.Store.entity.*;
import com.example.Store.entity.form.ProductForm;
import com.example.Store.service.ProductService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductDAOImplTest {

    //@Test
    public void resourceGetTest() throws URISyntaxException {

        Path p1 = Paths.get(this.getClass().getResource("/config/product_list.xml").toURI());
        System.out.println(p1.toString());
    }

    //@Test
    public void saveAndReturnTest(){
        try {
            var dao = new ProductDAOImpl();
            var service = new ProductService(dao);

            var product = new ProductForm();
            product.setId(10000L);
            product.setName("test_product");
            product.setDescription("this is description");
            product.setPrice(100.0);
            product.setSale(false);
            product.setProductCategoryEnum(ProductCategoryEnum.OTHER);
            product.setProductAvailability(ProductAvailability.AVAILABLE);

            service.createRecordProduct(product).subscribe(product1 -> {
                System.out.println("Complete");
            }, throwable -> {
                System.out.println("Exception " + throwable.getMessage());
            });
            Thread.sleep(100);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //@Test
    public void findAllTest() {
        var dao = new ProductDAOImpl();
        var service = new ProductService(dao);
        service.findAll().delayElements(Duration.ofSeconds(1)).subscribe(product -> System.out.println(product.getId()));
        try{
            Thread.sleep(7000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    //@Test
    public void saveAndDeleteTest(){ //тест валидный только если список продуктов пуст
        try{
            var product = new Product();
            product.setId(10000L);
            product.setName("test_product");
            product.setDescription("this is description");
            product.setPrice(100.0);
            product.setProductAvailability(ProductAvailability.AVAILABLE);

            var product1 = new Product();
            product1.setId(10001L);
            product1.setName("test_product1");
            product1.setDescription("this is description");
            product1.setPrice(111.0);
            product1.setProductAvailability(ProductAvailability.AVAILABLE);

            var productColor = new ProductColor();
            productColor.setColor("green");
            productColor.setPathToImage(List.of("./temp"));
            var productColor1 = new ProductColor();
            productColor1.setColor("red");
            productColor1.setPathToImage(List.of("./temp"));


            product.setListProductColor(Arrays.asList(productColor, productColor1));
            product1.setListProductColor(List.of(productColor));

            var dao = new ProductDAOImpl();

            dao.save(product);
            dao.save(product1);

            List<Product> productList = dao.findAllProduct().collectList().block();
            assert productList != null;
            assertEquals(productList.size(), 2); //было сохранено 2 записи

            dao.delete(10000L).subscribe();

            Thread.sleep(100);
            List<Product> productList1 = dao.findAllProduct().collectList().block();
            assert productList1 != null;
            assertEquals(productList1.get(0).getId(), 10001L); //id оставшейся записи
            assertEquals(productList1.size(), 1); //была удалена одна запись

            dao.delete(10001L).subscribe();

            Thread.sleep(100);
            List<Product> productList2 = dao.findAllProduct().collectList().block();
            assert productList2 != null;
            assertEquals(productList2.size(), 0); //была удалена последняя запись

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //@Test
    public void marshallerTest(){
        try{
            String PRODUCT_MARKET_XML = "./src/main/resources/config/product_list_test.xml";
            var market = new Market();
            var productHashMap = new HashMap<Long, Product>();

            var product = new Product();
            product.setId(10000L);
            product.setName("test_product");
            product.setPrice(10.0);
            product.setDescription("this is description");
            product.setProductAvailability(ProductAvailability.AVAILABLE);
            product.setSale(true);
            product.setPriceSale(5.0);

            var productColor = new ProductColor();
            productColor.setColor("green");
            productColor.setPathToImage(List.of("/temp"));
            product.setListProductColor(List.of(productColor));
            productHashMap.put(product.getId(), product);

            var product1 = new Product();
            product1.setId(1001L);
            product1.setName("Byd Han");
            product1.setPrice(1_880_000.0);
            product1.setDescription("Доставка по городу Бишкек – Бесплатная. Доставка по КР обговаривается отдельно.");
            product1.setProductAvailability(ProductAvailability.AVAILABLE);
            product1.setSale(true);
            product1.setPriceSale(5.0);

            var productColor1 = new ProductColor();
            productColor1.setColor("red");
            productColor1.setPathToImage(List.of("/temp"));
            product1.setListProductColor(List.of(productColor1));
            productHashMap.put(product1.getId(), product1);

            market.setProductMap(productHashMap);

            //перезаписать в файл
            var context = JAXBContext.newInstance(Market.class);
            var marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(market, new File(PRODUCT_MARKET_XML));

        }catch (JAXBException e){
            e.printStackTrace();
        }
    }
}
