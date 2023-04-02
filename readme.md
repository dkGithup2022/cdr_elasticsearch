### Elasticsearch module

spring-data-elasticsearch 를 이용한 프로젝트의 doc 클래스, dao 클래스에 대한 모음입니다. 
(~4/1 까지의 작업 . 이후 migration 을 마치고 나머지 읽기 요청에 대한 함수 작업 예정  )

---
</br>
#### 기능

1. data type 정의. 
2. DAO 공통 기능 정의

```java
// 공통 기능 : index, index with id, findAll 
@Repository
@RequiredArgsConstructor
public abstract class ElasticsearchRepository<T> {
    private final ElasticsearchOperations elasticsearchOperations;

    public T index(String indexName, T document) {
        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        return elasticsearchOperations.save(document, indexCoordinates);
    }

    public T index(String indexName, String id, T document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(id) // 문서 아이디 지정
                .withObject(document)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
        String response = elasticsearchOperations.index(indexQuery, indexCoordinates);

        if (response == null) {
            throw new RuntimeException("Operation response is null");
        }

        return (T) document;
    }


    public Page<T> findAll(String indexName, Pageable pageable) {

        Query query = Query.findAll();
        query.setPageable(pageable);

        SearchHits<T> searchHits = elasticsearchOperations.search(query, getDocType(), IndexCoordinates.of(indexName));
        SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        return (Page<T>) SearchHitSupport.unwrapSearchHits(page);
    }

    public abstract Class getDocType();
}
```
</br>
---

### 테스트 
"같은 아이디로 여러번  index", "데이터 조회 결과 비교" 등 단위테스트보다 통합테스트에 신경 써야할 내용이 많습니다. 아래의 내용을 기록합니다. 

- testcontainer 적용
```java
    //  /test/integration/ElasticTestContainer
  public class ElasticTestContainer extends ElasticsearchContainer {
    private static final String DOCKER_ELASTIC = "docker.elastic.co/elasticsearch/elasticsearch:7.9.1";

    private static final String CLUSTER_NAME = "test-node";

    private static final String ELASTIC_SEARCH = "elasticsearch";

    public ElasticTestContainer() {
        super(DOCKER_ELASTIC);

        this.addFixedExposedPort(9200, 9200);
        this.addFixedExposedPort(9300, 9300);
        this.withEnv("ES_JAVA_OPTS", "-Xms256m -Xmx512m -XX:MaxDirectMemorySize=536870912");
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
        this.withEnv("discovery.type", "single-node");

        String regex = ".*(\"message\":\\s?\"started\".*|] started\n$)";

        this.setWaitStrategy((new LogMessageWaitStrategy())
                .withRegEx(regex)
                .withStartupTimeout(Duration.ofSeconds(180L)));
    }
}
// 통합테스트용 APP.JAVA

```

</br>


```java
// 통합 테스트 공통 부분 .

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpbitCandleRepositoryTest {

    @Container
    private static ElasticTestContainer esContainer = new ElasticTestContainer();

    @Autowired
    private UpbitCandleRepository candleRepository;

    private static EsIndexOps esIndexOps = new EsIndexOps();

    @BeforeAll
    static void setUp() {
        esContainer.start();
    }
    ....

```

---
### 메모
1. EsIndexUtil 함수를 통해 인덱스를 지우고 새로 생성해도 이전 인덱스의 데이터가 남아 있는 경우가 있음.

   -> 현재는 통합 테스트 시 검사해야할 인덱스를 번갈아면서 하지만 조치 필요 .

</br>

2. EsIndexUtil 클래스에서 ForceMerge 함수는 ElaticsearchOperations 에서 제공하지 않음.
   
    -> 현재는 RestTemplate 로 직접 api 를 작성하지만 추후 업데이트 필요. 
   


</br>

3. 위에 공통 부분 태그나 SUPER class 로 묶을 수 있으면 좋을텐데,
    
    -> 적당한 용례를 못찾겠음. 추후 업데이트 필요 
