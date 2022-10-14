<template>
  <div class="staff-container">
    <Row class="staff-searchBar">
      <Col span="8"></Col>
      <Col span="8"></Col>
      <Col span="8">
        <!-- 搜索 -->
        <Input
          v-model="searchParam.keyword"
          clearable
          search
          @on-search="getTableData"
          @on-clear="clearKeyword"
        >
          <span slot="prepend">根据姓名查询</span>
        </Input>
      </Col>
    </Row>

    <!-- 表格主体 -->
    <Table
      ref="mainTable"
      border
      :key="tableUpdateKey"
      stripe
      v-bind:data="tableData"
      :columns="tableColumns"
      row-key="id"
      @on-sort-change="reSortTableData"
      @on-column-width-resize="saveTableState"
    >
      <template slot-scope="{ row }" slot="action">
        <Button
          type="success"
          size="small"
          style="margin-right: 5px"
          @click="edit(row)"
          >修改</Button
        >
        <Button type="error" size="small" @click="remove(row)">删除</Button>
      </template>
    </Table>

    <!-- 分页 -->
    <div style="margin: 10px; overflow: hidden">
      <div style="float: right">
        <Page
          show-total
          show-elevator
          :total="this.total"
          :current="this.searchParam.current"
          :page-size="this.searchParam.size"
          @on-change="changePage"
        >
        </Page>
      </div>
    </div>
  </div>
</template>
  
 <script>
import staffApi from "@/api/staff";
import Sortable from "sortablejs";

export default {
  name: "StaffList",
  data() {
    return {
      total: 0, // 总记录数
      searchParam: {
        keyword: "", // 查询关键字
        current: 1, // 页码
        size: 10, // 每页记录数
        field: "name", // 查询条件
        sortBy: "gmt_modified", // 排序条件
        sortAsc: false, // 正序排序
      },
      tableUpdateKey: 0, // 数据表表头调整后刷新
      tableData: [], // 表格数据
      tableColumns: [
        {
          title: "姓名",
          key: "name",
          sortable: true,
          resizable: true,
          width: 180,
        },
        {
          title: "性别",
          key: "sex",
          resizable: true,
          width: 180,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          resizable: true,
          width: 180,
        },
        {
          title: "部门",
          key: "department",
          sortable: "custom",
          resizable: true,
          width: 180,
          render: (h, params) => {
            const row = params.row;
            const text =
              row.department === 1
                ? "业务部"
                : row.department === 2
                ? "采购部"
                : row.stadepartmenttus === 3
                ? "行政部"
                : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "学历",
          sortable: "custom",
          resizable: true,
          width: 180,
          key: "formalSchooling",
          render: (h, params) => {
            const row = params.row;
            const text =
              row.formalSchooling === 1
                ? "初中"
                : row.formalSchooling === 2
                ? "高中"
                : row.formalSchooling === 3
                ? "大专"
                : "本科";
            return h("Tag", {}, text);
          },
        },
        {
          title: "邮箱",
          key: "email",
          resizable: true,
          width: 180,
        },
        {
          title: "操作",
          slot: "action",
          resizable: true,
          width: 150,
          align: "center",
        },
      ],
    };
  },
  created() {
    // 当页面加载时获取数据
    this.getTableData();
  },
  mounted() {
    this.getTableState();
    this.columnDrop();
    console.log(this.tableColumns);
  },
  methods: {
    getTableData() {
      staffApi.staffComplexQuery(this.searchParam).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.tableData = response.data.items;
          this.total = response.data.total;
        }
      });
    },
    //列拖拽
    columnDrop() {
      const wrapperTr =
        this.$refs.mainTable.$refs.header.firstChild.children[1].children[0];

      this.sortable = Sortable.create(wrapperTr, {
        animation: 180,
        delay: 0,
        onEnd: (evt) => {
          const oldItem = this.tableColumns[evt.oldIndex];
          this.tableColumns.splice(evt.oldIndex, 1);
          this.tableColumns.splice(evt.newIndex, 0, oldItem);
          this.tableUpdateKey += 1;

          this.saveTableState();
          this.$nextTick(function () {
            this.columnDrop();
          });
        },
      });
    },
    changePage(page) {
      this.searchParam.current = page;
      this.getTableData();
    },
    clearKeyword() {
      this.searchParam.keyword = "";
      this.getTableData();
    },
    search(keyword) {
      this.searchParam.keyword = keyword;
      this.getTableData();
    },
    reSortTableData(column) {
      switch (column.key) {
        case "name":
          this.searchParam.sortBy = "name";
          break;
        case "department":
          this.searchParam.sortBy = "department";
          break;
        case "formalSchooling":
          this.searchParam.sortBy = "formal_schooling";
          break;
        default:
          this.searchParam.sortBy = "gmt_modified";
      }
      this.searchParam.sortAsc = column.order == "asc";
      this.getTableData();
    },
    edit(row) {
      this.$router.push({ name: "StaffEdit", params: { id: row.id } });
    },
    remove(row) {
      let dataform = { id: row.id };
      staffApi.delete(dataform).then((response) => {
        if (response.success === true) {
          this.getTableData();
        }
      });
    },
    saveTableState() {
      let columnAttribute = this.tableColumns.map((obj, index) => {
        let t = {};
        t.situation = index;
        t.key = obj.key;
        t.width = obj.width;
        return t;
      });
      this.$store.commit("staffList/SET_COLUMNATTRIBUTE", columnAttribute);
    },
    getTableState() {
      let columnAttribute = this.$store.state.staffList.columnAttribute;
      if (columnAttribute.length !== 0) {
        let newTableColumns = [];

        this.tableColumns.forEach((item) => {
          const t = columnAttribute.find((i) => i.key === item.key);
          item.width = t.width;
          newTableColumns[t.situation] = item;
        });

        this.tableColumns = newTableColumns;
      }
    },
  },
};
</script>
  
  <style lang="scss" scoped>
.staff {
  &-container {
    margin: 10px;
  }
  &-searchBar {
    margin-bottom: 5px;
  }
}
</style>
  