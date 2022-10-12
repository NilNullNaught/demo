<template>
  <div class="staff-container">
    <Row class="staff-searchBar">
      <Col span="8"></Col>
      <Col span="8">
        <div v-if="this.$route.params.id">
          <div class="staff-title">修改员工资料</div>
        </div>
        <div v-else>
          <div class="staff-title">添加新员工资料</div>
        </div>
        <Form
          ref="formValidate"
          :model="formValidate"
          :rules="ruleValidate"
          :label-width="80"
        >
          <FormItem label="姓名" prop="name">
            <Input
              @on-change="saveState"
              v-model="formValidate.name"
              placeholder="请输入姓名"
            ></Input>
          </FormItem>
          <FormItem label="性别" prop="sex">
            <RadioGroup @on-change="saveState" v-model="formValidate.sex">
              <Radio :label="1">男</Radio>
              <Radio :label="2">女</Radio>
            </RadioGroup>
          </FormItem>

          <FormItem label="身份证号" prop="idcardNumber">
            <Input
              @on-change="saveState"
              v-model="formValidate.idcardNumber"
              placeholder="输入身份证号"
            ></Input>
          </FormItem>

          <FormItem label="部门" prop="department">
            <RadioGroup
              @on-change="saveState"
              v-model="formValidate.department"
            >
              <Radio :label="1">业务部</Radio>
              <Radio :label="2">采购部</Radio>
              <Radio :label="3">行政部</Radio>
            </RadioGroup>
          </FormItem>

          <FormItem label="学历" prop="formalSchooling">
            <RadioGroup
              @on-change="saveState"
              v-model="formValidate.formalSchooling"
            >
              <Radio :label="1">初中</Radio>
              <Radio :label="2">高中</Radio>
              <Radio :label="3">大专</Radio>
              <Radio :label="4">本科</Radio>
            </RadioGroup>
          </FormItem>

          <FormItem label="邮箱" prop="mail">
            <Input
              @on-change="saveState"
              v-model="formValidate.mail"
              placeholder="输入邮箱地址"
            ></Input>
          </FormItem>

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
      <Col span="8"></Col>
    </Row>
  </div>
</template>
  
  <script>
import staffApi from "@/api/staff";
import { mapState, mapGetters, mapMutations } from "vuex";

export default {
  name: "StaffForm",
  //第一种
  computed: {
    formValidate() {
      if (this.$store.state.staffForm.formValidate) {
        return this.$store.state.staffForm.formValidate;
      } else {
        return {
          id: "0",
          name: "",
          sex: 0,
          idcardNumber: "",
          department: 0,
          formalSchooling: 0,
          mail: "",
        };
      }
    },
  },
  data() {
    const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    const validateidcardNumber = (rule, value, callback) => {
      // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
      if (reg.test(value) === false) {
        callback(new Error("身份证号格式错误"));
      }
      callback();
    };
    return {
      ruleValidate: {
        name: [
          {
            required: true,
            message: "姓名不能为空",
            trigger: "blur",
          },
        ],
        idcardNumber: [
          {
            required: true,
            message: "身份证号不能为空",
            trigger: "blur",
          },
          {
            validator: validateidcardNumber,
            trigger: "blur",
          },
        ],
        mail: [{ type: "email", message: "邮箱格式错误", trigger: "blur" }],
      },
    };
  },
  created() {
    let id = this.$store.state.staffForm.formValidate.id;
    console.log("id" + id);

    if (this.$route.params.id) {
      // 修改员工资料
      if (this.$route.params.id != id) {
        // 不是之前正在进行的修改，重新获取数据
        this.query();
      }
    } else {
      // 新增员工资料
      if (id != "0") {
        // 之前进行过未完成的修改，需要清除
        this.$store.commit("staffForm/SET_FORMVALIDATE");
      }
    }
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
    add() {
      staffApi.add(this.formValidate).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.$Message.success("新增成功");
          this.$store.dispatch("staffForm/RESET_FORMVALIDATE");
          this.$router.push("/staff/list");
        }
      });
    },
    edit() {
      this.formValidate.id = this.$route.params.id;
      staffApi.edit(this.formValidate).then((response) => {
        // debugger 设置断点调试
        if (response.success === true) {
          this.$Message.success("修改成功");
          this.$store.dispatch("staffForm/RESET_FORMVALIDATE");
          this.$router.push("/staff/list");
        }
      });
    },
    saveState() {
      this.$store.dispatch("staffForm/SET_FORMVALIDATE", this.formValidate);
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
  