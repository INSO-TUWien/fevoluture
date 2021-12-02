package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends ArangoRepository<Package, String> {

    Optional<Package> findOneByName(String name);
    List<Package> findAllByNameIn(List<String> names);

    @Query("FOR ele in @packageList\n" +
            "    FOR p in packages\n" +
            "     FILTER p.name == ele.name \n" +
            "        RETURN p")
    List<Package> findAllByNames(List<Package> packageList);
}

