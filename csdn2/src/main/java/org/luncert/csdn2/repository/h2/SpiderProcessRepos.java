package org.luncert.csdn2.repository.h2;

import org.luncert.csdn2.model.h2.SpiderProcess;
import org.luncert.csdn2.model.normal.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpiderProcessRepos extends JpaRepository<SpiderProcess, String>
{

    SpiderProcess findByCategory(Category category);

}