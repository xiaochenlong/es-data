package vip.corejava.sample.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.GeoDistanceOrder;
import vip.corejava.sample.City;
import vip.corejava.sample.CityRepository;


/**
 * @author xcl
 * @date 2023/5/13
 */
@SpringBootTest
@Slf4j
public class EsTest {

    @Resource
    CityRepository cityRepository;

    @Resource
    ElasticsearchOperations operations;

    @Test
    void init() {
        //初始化数据
        for (long i = 0; i < 100; i++) {
            City city = new City();
            city.id = i;
            city.name = "上海" + i;
            city.location = new GeoPoint(22.815256, 113.802491);
            City save = cityRepository.save(city);
            log.info("-----{}", save);
        }
    }

    @Test
    void s() {
        //查询全部
        Iterable<City> all = cityRepository.findAll();
        all.forEach(e -> log.info("->{}", e));
    }

    @Test
    void s2() {
        //查询条件
        Criteria criteria = new Criteria("id").greaterThan(20).lessThan(90).or("name").is("上海99");
        CriteriaQuery query = CriteriaQuery.builder(criteria).build();

        //定义距离排序
        Sort sort = Sort.by(new GeoDistanceOrder("location", new GeoPoint(48.137154, 11.5761247)));
        query.addSort(sort);
        query.addSort(Sort.by("id").descending());
        //分页信息
        query.setPageable(PageRequest.of(0, 10));
        //执行查询
        SearchHits<City> search = operations.search(query, City.class);
        search.get().forEach(e -> {
            log.info("->{}", e);
        });
    }
}
