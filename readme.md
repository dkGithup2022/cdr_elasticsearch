### Elasticsearch module

spring-data-elasticsearch 를 이용한 프로젝트의 doc 클래스, dao 클래스에 대한 모음입니다.

---

</br>

### 기능

1. data type 정의. 
2. DAO 공통 기능 정의
3. dao 구현체 클래스 작성


### Data type

- bithumb
  - bithumbTickRepository
  - bithumbOrderbookRepository
  - bithumbCandleRepository


</br>

- upbit
   - upbitTickRepository
   - upbitOrderbookRepository
   - upbitCandleRepository



###  Abstraction

공통 제공 기능
1. index ( T doc )
2. index (id)
3. findAll
4. bulk insert 

</br>

```java
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

      return (T)document;
   }

   public Page<T> findAll(String indexName, Pageable pageable) {

      Query query = Query.findAll();
      query.setPageable(pageable);

      SearchHits<T> searchHits = elasticsearchOperations.search(query, getDocType(), IndexCoordinates.of(indexName));
      SearchPage<T> page = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

      return (Page<T>)SearchHitSupport.unwrapSearchHits(page);
   }
   
   public List<IndexedObjectInformation> bulkIndex(List<T> documents) throws JsonProcessingException {
      // check if empty
      if (documents.size() == 0)
         return null;

      HashMap<String, ArrayList<IndexQuery>> queryMap = new HashMap<>();

      // seperate bulk req by T.getCode() or T.getMarket();
      documents.stream().forEach(
              (T doc) -> {
                 // check if instance of code ;
                 checkType(doc);
                 try {
                    queryMap.computeIfAbsent(getDocStrCode(doc), e -> new ArrayList<IndexQuery>())
                            .add(buildIndexQuery(doc));
                 } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                 }
              });

      List<IndexedObjectInformation> result = new ArrayList<>();

      // send each bulk;
      for (String code : queryMap.keySet())
         result.addAll(elasticsearchOperations.bulkIndex(queryMap.get(code), genIndexCoordinate(code)));


      return result;
   }

   protected abstract IndexCoordinates genIndexCoordinate(String code);

   private void checkType(T doc) {
      if (getDocType().isInstance(doc))
         return;
      throw new RuntimeException(
              "There is type misMatch : generic T does not contain document |  T : " + getDocType() + " | doc: "
                      + doc.getClass());
   }

   protected abstract String getDocStrCode(T doc);

   public abstract IndexQuery buildIndexQuery(T doc) throws JsonProcessingException;

   public abstract Class getDocType();
}
```

#### abstract functions 

type 의존적이거나 연산들 ( 타입에 따라 함수 구현체가 다른 경우) 는 abstract 함수로 정의합니다.

1. getDocType(); 
   - es operation 에서 Clazz 를 명시해야 하는 경우 있음. 이건 각 구현체에서 각자 다루는 Doc type 을 리턴하도록 함.



2. genIndexCoordinate,  getDocStrCode, buildIndexQuery
   - bulk 요청에 쓰이는 함수들, 각 타입별로 수행해야 하는 함수를 다형적으로 표현이 안된다 ( 호출 함수 자체가 다른 경우가 있음. )
   - 고려중인 사항은 , 저렇게 공통부분이 더러워질거면  bulk operation 자체를 abstract 하게 작성하는게 더 깔끔하지는 않을까 생각중 . 

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

1.  insert 관련연산 의 통합 테스트 시,  insert 된 결과가 forceMerge 를 사용해도 바로 조회되지 않음.

      -> 현재 갯수 관련 test 를 전부 주석 처리 해놨는데, 남들 어떻게 하는지 조사 필요.

      -> 긴 시간의 sleep 요청을 두는 경우 , parametized test 에서 시간 너무 많이 소요 .




</br>

2. elasticsearchOperations 에서 쿼리를 직접 작성 시 응답이 변수에 매핑되지 않음 .
   -> spring data elasticsearch  에서 저렇게 밖에 지원 안하면 return type void 로 변경     
