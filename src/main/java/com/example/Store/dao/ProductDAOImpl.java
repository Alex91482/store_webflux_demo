package com.example.Store.dao;

import com.example.Store.dao.interfaces.ProductDAO;
import com.example.Store.entity.Market;
import com.example.Store.entity.Product;
import com.example.Store.entity.ProductCategoryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductDAOImpl implements ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);
    private Path PRODUCT_MARKET_XML_PATH;

    public ProductDAOImpl(){
        try{
            PRODUCT_MARKET_XML_PATH = Paths.get("resource/config/product_list.xml");
            logger.info("Path is valid: {}", PRODUCT_MARKET_XML_PATH.toString());
        }catch (Exception e){
            logger.error("ProductDAOImpl fatal Error during initialization: {}", e.getMessage());
        }
    }

    @Override
    public void save(Product product){
        update(product).subscribe(
                complete -> logger.info("Save product to xml file, id = {}, product name = {}", product.getId(), product.getName()),
                error -> logger.error("Product save failure, id = {}, product name = {}", product.getId(), product.getName())
        );
    }

    @Override
    public Mono<Void> update(Product product){
        return Mono.create(event -> {
            try{
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                var productMap = market.getProductMap() != null ? market.getProductMap() : new HashMap<Long, Product>();
                productMap.put(product.getId(), product); //записываем в мапу новый продукт либо обновляем существующий
                market.setProductMap(productMap); //записываем мапу с продуктами в маркет
                //перезаписать в файл
                var marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(market, new File(PRODUCT_MARKET_XML_PATH.toUri()));

                event.success(); //выполнено успешно

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error save or update : {}", e.getMessage());
                event.error(e); //неудача, передаем ошибку
            }
        });
    }

    @Override
    public Mono<Product> saveAndReturnProduct(Product product){
        return Mono.create(event -> {

            try{
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                var productMap = market.getProductMap() != null ? market.getProductMap() : new HashMap<Long, Product>();
                productMap.put(product.getId(), product); //записываем в мапу новый продукт либо обновляем существующий
                market.setProductMap(productMap); //записываем мапу с продуктами в маркет
                //перезаписать в файл
                var marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(market, new File(PRODUCT_MARKET_XML_PATH.toUri()));

                event.success(product); //выполнено успешно

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error save or update : {}", e.getMessage());
                event.error(e); //неудача, передаем ошибку
            }
        });
    }

    @Override
    public Mono<Void> delete(Long id){
        if(id == null){
            return Mono.empty();
        }
        return Mono.create(event -> {
            try{
                //демаршалинг xml файла
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //основная обработка
                var productMap = market.getProductMap();
                productMap.remove(id);
                market.setProductMap(productMap);
                //перезаписать в файл
                var marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(market, new File(PRODUCT_MARKET_XML_PATH.toUri()));

                event.success(); //событие завершенно успешно

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error delete, id : {}, error : {}", id, e.getMessage());
                event.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Mono<Product> findById(Long id){
        if(id == null){
            return Mono.empty();
        }
        return Mono.create(event -> {
            var product = new Product();
            try {
                //демаршалинг
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //извлекаем по id продукт из map
                var productPush = market.getProductMap().get(id);
                if(productPush != null){
                    product = productPush;
                }

            } catch (JAXBException | FileNotFoundException e) {
                logger.error("Error unmarshalling : {}", e.getMessage());
                event.error(new RuntimeException(e));
            }
            event.success(product); //возвращаем результат (может вернуть пустой продукт?)
        });
    }

    @Override
    public Flux<Product> findByName(String name){
        return Flux.create(event -> {
            List<Product> list = new ArrayList<>();
            try {
                //демаршалинг
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //получаем все продукты подходящие под запрос
                market.getProductMap().forEach((key, value) -> {
                    if(value.getName().equals(name)){
                        list.add(value);
                    }
                });
                //создаем последовательность
                for(Product product : list){
                    event.next(product); //перебираем и отдаем все продукты которые получили
                }

                event.complete(); //сигнал о завершении последовательности

            } catch (JAXBException | FileNotFoundException e) {
                logger.error("Error unmarshalling : {}", e.getMessage());
                event.error(new RuntimeException(e)); //сигнал о ошибке
            }
        });
    }

    @Override
    public Flux<Product> findAllProduct(){
        return Flux.create(event -> {
            List<Product> list = new ArrayList<>();
            try {
                //демаршалинг
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //извлекаем все значения
                list = new ArrayList<>(market.getProductMap().values());
                //создаем последовательность
                for(Product product : list){
                    event.next(product); //перебираем и отдаем все продукты которые получили
                }

                event.complete(); //сигнал о завершении последовательности

            } catch (JAXBException | FileNotFoundException e) {
                logger.error("Error unmarshalling : {}", e.getMessage());
                event.error(new RuntimeException(e)); //сигнал о ошибке
            }
        });
    }

    @Override
    public Flux<Product> findAllProductByCategory(ProductCategoryEnum productCategoryEnum){
        return Flux.create(event -> {
            try{
                var list = new ArrayList<Product>();
                //демаршалинг
                var context = JAXBContext.newInstance(Market.class);
                var um = context.createUnmarshaller();
                var market = (Market) um.unmarshal(
                        new InputStreamReader(
                                new FileInputStream(PRODUCT_MARKET_XML_PATH.toFile()), StandardCharsets.UTF_8)
                );
                //извлекаем все значения
                list = new ArrayList<>(market.getProductMap().values());
                //создаем последовательность
                for(Product product : list){
                    if(product.getProductCategoryEnum().equals(productCategoryEnum)){
                        event.next(product); //перебираем и отдаем все продукты которые подходят
                    }
                }

                event.complete(); //сигнал о завершении последовательности

            }catch (JAXBException | FileNotFoundException e) {
                logger.error("Error unmarshalling : {}", e.getMessage());
                event.error(new RuntimeException(e)); //сигнал о ошибке
            }
        });
    }
}
