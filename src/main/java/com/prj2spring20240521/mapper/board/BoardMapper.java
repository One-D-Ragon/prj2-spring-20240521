package com.prj2spring20240521.mapper.board;

import com.prj2spring20240521.domain.board.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("""
            INSERT INTO board (title, content, member_id)
            VALUES (#{title}, #{content}, #{memberId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    /* insert 되자마자 id 프로퍼티를 알려달라 */
    public int insert(Board board);

    @Select("""
            SELECT b.id, b.title, m.nick_name writer
            FROM board b JOIN member m ON b.member_id = m.id
            ORDER BY id DESC
            """)
    List<Board> selectAll();

    @Select("""
            SELECT b.id, b.title, b.content, b.inserted, m.nick_name writer, b.member_id
            FROM board b JOIN member m ON b.member_id = m.id
            WHERE b.id = #{id}
            """)
    Board selectById(Integer id);

    @Delete("""
            DELETE FROM board
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            UPDATE board
            SET title=#{title}, content=#{content}
            WHERE id = #{id}
            """)
    int update(Board board);

    @Delete("""
            DELETE FROM board
            WHERE member_id=#{memberId}
            """)
    int deleteByMemberId(Integer memberId);

    @Select("""
            <script>
            SELECT b.id, b.title, m.nick_name writer
            FROM board b JOIN member m ON b.member_id = m.id
                <trim prefix="WHERE" prefixOverrides="OR">
                <if test="searchType != null">
                    <bind name="pattern" value="'%' + keyword + '%'" />
                    <if test="searchType == 'all' || searchType == 'text'">
                        OR b.title LIKE #{pattern}
                        OR b.content LIKE #{pattern}
                    </if>
                    <if test="searchType == 'all' || searchType == 'nickName'">
                        OR m.nick_name LIKE #{pattern}
                    </if>
                </if>
                </trim>
            ORDER BY id DESC
            LIMIT #{offset}, 10
            </script>
            """)
    List<Board> selectAllPaging(Integer offset, String searchType, String keyword);

    @Select("""
            SELECT COUNT(*) FROM board
            """)
    Integer countAll();

    @Select("""
            <script>
            SELECT COUNT(b.id)
            FROM board b JOIN member m ON b.member_id = m.id
                <trim prefix="WHERE" prefixOverrides="OR">
                <if test="searchType != null">
                    <bind name="pattern" value="'%' + keyword + '%'" />
                    <if test="searchType == 'all' || searchType == 'text'">
                        OR b.title LIKE #{pattern}
                        OR b.content LIKE #{pattern}
                    </if>
                    <if test="searchType == 'all' || searchType == 'nickName'">
                        OR m.nick_name LIKE #{pattern}
                    </if>
                </if>
                </trim>
            </script>
            """)
    Integer countAllWithSearch(String searchType, String keyword);

    @Insert("""
            INSERT INTO board_file (board_id, name)
            VALUES (#{boardId}, #{name})
            """)
    int insertFileName(Integer boardId, String name);
}

