package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Method;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

import java.util.List;
import java.util.Optional;

public interface MethodRepository extends ArangoRepository<Method, String> {

    Optional<Method> findOneByNameAndFileId(String name, String fileId);


    @Query("FOR ele in @methodList\n" +
            "    FOR m in methods\n" +
            "     FILTER m.name == ele.name AND m.file == ele.file\n" +
            "        RETURN m")
    List<Method> findAllByNameAndFileId(List<Method> methodList);
}
