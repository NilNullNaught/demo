<template>
  <div class="staff-container">
    <Row class="staff-searchBar">
      <Col span="6"></Col>
      <Col span="12">
        <div v-if="this.$route.params.id">
          <div class="staff-title">修改培训记录</div>
        </div>
        <div v-else>
          <div class="staff-title">添加培训记录</div>
        </div>
        <Form
          ref="formValidate"
          :model="formValidate"
          :label-width="80"
          @on-validate="saveState"
        >
          <FormItem label="培训老师" prop="trainingTeacher">
            <Select
              @on-change="saveState"
              v-model="formValidate.trainingTeacher"
              placeholder="请选择培训老师"
            >
              <Option
                v-for="teacher in teachers"
                :key="teacher"
                :value="teacher"
                >{{ teacher }}</Option
              >
            </Select>
          </FormItem>

          <FormItem label="培训日期" prop="trainingDate">
            <DatePicker
              @on-change="saveStateS"
              v-model="formValidate.trainingDate"
              type="date"
              placeholder="请选择培训日期"
            ></DatePicker>
          </FormItem>

          <FormItem label="培训内容" prop="trainContent">
            <Input
              @on-change="saveState"
              v-model="formValidate.trainContent"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 8 }"
              placeholder="请输入培训内容"
            ></Input>
          </FormItem>

          <FormItem label="参与人员">
            <div>
              <Table
                border
                size="small"
                ref="selection"
                :columns="selectTableColumns"
                :data="selectTableData"
                @on-selection-change="addOrRemoveItems"
                @on-select-all="addOrRemoveItems"
              ></Table>

              <Page
                size="small"
                :total="this.total"
                :current="this.selectTableSearchParam.current"
                :page-size="this.selectTableSearchParam.size"
                @on-change="changePage"
              >
              </Page>
              <div>
                <Table
                  border
                  size="small"
                  ref="selection"
                  :columns="displayTableColumns"
                  :data="list"
                ></Table>
              </div>
            </div>
          </FormItem>

          <!-- <div>

              <Table
                border
                ref="selection2"
                :columns="columns3"
                :data="data2"
              ></Table>
            </div> -->

          <FormItem>
            <Button type="primary" @click="handleSubmit('formValidate')"
              >提交</Button
            >
            <Button
              @click="handleReset('formValidate')"
              style="margin-left: 8px"
              >重置</Button
            >
          </FormItem>
        </Form>
      </Col>
      <Col span="6"></Col>
    </Row>
  </div>
</template>
  
  <script>
import staffApi from "@/api/staff";
import { TransitionGroupStub } from "@vue/test-utils";
import { mapState, mapGetters, mapMutations } from "vuex";

export default {
  name: "StaffForm",
  //第一种
  computed: {
    formValidate() {
      if (this.$store.state.trainingRecord.formValidate) {
        return this.$store.state.trainingRecord.formValidate;
      } else {
        return {
          trainingTeacher: "",
          trainingDate: new Date(),
          trainContent: "",
          list: [],
        };
      }
    },

  },
  data() {
    return {
      total: 0,
      teachers: [],
      selectTableData: [],
      list: this.$store.state.trainingRecord.list,
      selectTableColumns: [
        {
          type: "selection",
          width: 55,
          align: "center",
        },
        {
          title: "姓名",
          key: "name",
        },
        {
          title: "性别",
          key: "sex",
          minWidth: 30,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          minWidth: 80,
        },
        {
          title: "部门",
          key: "department",
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
      ],
      displayTableColumns: [
        {
          title: "姓名",
          key: "name",
        },
        {
          title: "性别",
          key: "sex",
          minWidth: 30,
          render: (h, params) => {
            const row = params.row;
            const text = row.sex === 1 ? "男" : row.sex === 2 ? "女" : "未设置";
            return h("Tag", {}, text);
          },
        },
        {
          title: "身份证号码",
          key: "idcardNumber",
          minWidth: 80,
        },
        {
          title: "部门",
          key: "department",
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
      ],
      selectTableSearchParam: {
        keyword: "", // 查询关键字
        current: 1, // 页码
        size: 5, // 每页记录数
        field: "name", // 查询条件
        sortBy: "gmt_modified", // 排序条件
        sortAsc: false, // 正序排序
      },
    };
  },
  created() {
    let id = this.$store.state.trainingRecord.formValidate.id;

    if (this.$route.params.id) {
      // 修改培训记录
      if (this.$route.params.id != id) {
        // 不是之前正在进行的修改，重新获取数据
        this.query();
      }
    } else {
      // 新增培训记录
      if (id != "0") {
        // 之前进行过未完成的修改，需要清除
        this.$store.commit("trainingRecord/RESET_FORMVALIDATE");
      }
    }
    this.queryAllName();
    this.getSelectTableData();
  },
  methods: {
    handleSubmit(formValidate) {
      this.$refs[formValidate].validate((valid) => {
        if (valid) {
          if (this.$route.params.id) {
            this.edit();
          } else {
            this.add();
          }
        } else {
          this.$Message.error("请按照格式填写数据");
        }
      });
    },
    handleReset(formValidate) {
      this.$refs[formValidate].resetFields();
    },
    changePage(page) {
      this.selectTableSearchParam.current = page;
      this.getSelectTableData();
    },

    addOrRemoveItems(selection) {
      let idArray = selection.map((item) => {
        return item.id;
      });

      let removeArray = this.selectTableData.filter((item) => {
        if (!idArray.includes(item.id)) {
          return item;
        }
      });

      let removeArrayId = removeArray.map((item) => {
        return item.id;
      });

      this.list = this.list.filter((item) => {
        if (removeArrayId.includes(item.id)) {
          return item;
        }
      });
      this.list = this.list.filter((item) => {
        if (idArray.includes(item.id)) {
          return item;
        }
      });
      this.list.push.apply(this.list, selection);
      console.log(this.list);
      this.$store.commit("trainingRecord/SET_LIST", this.list);
    },

    query() {
      let data = { id: this.$route.params.id };
      staffApi.query(data).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.$store.dispatch(
            "staffForm/SET_FORMVALIDATE",
            response.data.data
          );
        }
      });
    },
    queryAllName() {
      staffApi.queryAllName().then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.teachers = response.data.data;
        }
      });
    },
    getSelectTableData() {
      staffApi
        .staffComplexQuery(this.selectTableSearchParam)
        .then((response) => {
          // debugger 设置断点调试
          if (response.success === true) {
            this.selectTableData = response.data.items;
            this.total = response.data.total;
          }
        });
    },
    add() {
      // staffApi.add(this.formValidate).then((response) => {
      //   // debugger 设置断点调试
      //   if (response.success === true) {
      //     this.$Message.success("新增成功");
      //     this.$store.dispatch("staffForm/RESET_FORMVALIDATE");
      //     this.$router.push("/staff/list");
      //   }
      // });
    },
    edit() {
      // this.formValidate.id = this.$route.params.id;
      // staffApi.edit(this.formValidate).then((response) => {
      //   // debugger 设置断点调试
      //   if (response.success === true) {
      //     this.$Message.success("修改成功");
      //     this.$store.dispatch("staffForm/RESET_FORMVALIDATE");
      //     this.$router.push("/staff/list");
      //   }
      // });
    },
    saveState() {
      this.$store.commit("trainingRecord/SET_FORMVALIDATE", this.formValidate);
    },
    saveStateS() {
      this.formValidate.trainingDate = new Date(
        this.formValidate.trainingDate.getTime() + 8 * 60 * 60 * 1000
      );
      this.saveState();
    },
  },
};
</script>
  
  <style lang="scss" scoped>
.staff {
  &-container {
    margin: 30px;
  }
  &-title {
    font-size: 30px;
    line-height: 46px;
    text-align: center;
    margin-bottom: 30px;
  }
}
</style>