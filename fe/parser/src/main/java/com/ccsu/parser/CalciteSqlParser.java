package com.ccsu.parser;

import com.ccsu.error.CommonException;

import com.ccsu.profile.JobProfile;
import com.ccsu.profile.Phrase;
import com.ccsu.profile.ProfileUtil;
import com.google.common.base.Stopwatch;
import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlConformanceEnum;

import java.util.concurrent.TimeUnit;

import static com.ccsu.error.CommonErrorCode.SQL_PARSER_ERROR;

public class CalciteSqlParser implements SqlParser {

    private static final org.apache.calcite.sql.parser.SqlParser.Config CONFIG
            = org.apache.calcite.sql.parser.SqlParser.Config.DEFAULT
            .withQuoting(Quoting.DOUBLE_QUOTE)
            .withConformance(SqlConformanceEnum.MYSQL_5)
            .withQuotedCasing(Casing.UNCHANGED)
            .withUnquotedCasing(Casing.UNCHANGED)
            .withParserFactory(ExtendSqlParserImpl.FACTORY);

    @Override
    public SqlNode parse(String sql, JobProfile profile) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        org.apache.calcite.sql.parser.SqlParser parser
                = org.apache.calcite.sql.parser.SqlParser.create(ignoreEndSeal(sql), CONFIG);
        try {
            SqlNode sqlNode = parser.parseQuery();

            ProfileUtil.addParsePhraseJobProfile(profile,
                    Phrase.SQL_PARSER, stopwatch.elapsed(TimeUnit.NANOSECONDS), sqlNode);
            return sqlNode;
        } catch (org.apache.calcite.sql.parser.SqlParseException e) {
            throw new CommonException(SQL_PARSER_ERROR, "Sql syntax error: " + sql + "," + e.getMessage());
        }
    }

    @Override
    public SqlNode parse(String sql) {
        org.apache.calcite.sql.parser.SqlParser parser
                = org.apache.calcite.sql.parser.SqlParser.create(ignoreEndSeal(sql), CONFIG);
        try {
            return parser.parseQuery();
        } catch (org.apache.calcite.sql.parser.SqlParseException e) {
            throw new CommonException(SQL_PARSER_ERROR, "Sql syntax error: " + sql + "," + e.getMessage());
        }
    }

    private String ignoreEndSeal(String sql) {
        if (sql.endsWith(";")) {
            return sql.substring(0, sql.length() - 1);
        }
        return sql;
    }
}
