package program.rule.program;

import context.QueryContext;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rex.RexBuilder;
import program.program.RuleOptimizeProgram;
import program.rule.shuttle.SearchRemoveShuttle;

public class SearchRemoveProgram implements RuleOptimizeProgram {
    @Override
    public RelNode optimize(RelNode root, QueryContext queryContext) {
        RexBuilder rexBuilder = root.getCluster().getRexBuilder();;
        SearchRemoveShuttle searchRemoveShuttle = new SearchRemoveShuttle(rexBuilder);
        return root.accept(searchRemoveShuttle);
    }
}
